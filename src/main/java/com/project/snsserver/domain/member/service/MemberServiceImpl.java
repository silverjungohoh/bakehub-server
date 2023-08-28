package com.project.snsserver.domain.member.service;

import com.project.snsserver.domain.awss3.service.AwsS3Service;
import com.project.snsserver.domain.mail.model.MailMessage;
import com.project.snsserver.domain.mail.service.MailService;
import com.project.snsserver.domain.member.model.dto.SignUpRequest;
import com.project.snsserver.domain.member.model.dto.SignUpResponse;
import com.project.snsserver.domain.member.model.dto.VerifyAuthCodeRequest;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.model.entity.MemberAuthCode;
import com.project.snsserver.domain.member.repository.jpa.MemberRepository;
import com.project.snsserver.domain.member.repository.redis.MemberAuthCodeRepository;
import com.project.snsserver.domain.member.type.MemberRole;
import com.project.snsserver.domain.member.type.MemberStatus;
import com.project.snsserver.global.error.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.project.snsserver.global.error.type.MemberErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final String SUBJECT = "[회원 인증] 이메일 인증 코드 발송 안내";
    private static final String DIR = "profile";

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final MemberAuthCodeRepository memberAuthCodeRepository;
    private final AwsS3Service awsS3Service;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Map<String, String> checkEmailDuplicate(String email) {

        if(memberRepository.existsByEmail(email)) {
            throw new MemberException(DUPLICATED_EMAIL);
        }
        return getMessage("사용 가능한 이메일입니다.");
    }

    @Override
    public Map<String, String> checkNicknameDuplicate(String nickname) {

        if(memberRepository.existsByNickname(nickname)) {
            throw new MemberException(DUPLICATED_NICKNAME);
        }
        return getMessage("사용 가능한 닉네임입니다.");
    }

    @Override
    public Map<String, String> sendEmailAuthCode(String email) {

        String authCode = RandomStringUtils.randomAlphanumeric(12);

        MailMessage mail = MailMessage.builder()
                .to(email)
                .subject(SUBJECT)
                .message("인증 코드 : " + authCode)
                .build();

        mailService.sendMail(mail, authCode);

        MemberAuthCode code = MemberAuthCode.builder()
                .id(authCode)
                .email(email)
                .expiredAt(1000L * 60 * 3)
                .build();

        memberAuthCodeRepository.save(code);
        return getMessage("메일 인증번호를 전송하였습니다.");
    }

    @Override
    public Map<String, String> verifyEmailAuthCode(VerifyAuthCodeRequest request) {

        MemberAuthCode authCode = memberAuthCodeRepository.findById(request.getCode())
                .orElseThrow(() -> new MemberException(INCORRECT_EMAIL_AUTH_CODE));

       if(!Objects.equals(request.getEmail(), authCode.getEmail())) {
           throw new MemberException(INCORRECT_EMAIL_AUTH_CODE);
       }

        return getMessage("메일 인증에 성공하였습니다.");
    }

    @Override
    @Transactional
    public SignUpResponse signUp(MultipartFile file, SignUpRequest request) {

        if(!Objects.equals(request.getPasswordCheck(), request.getPassword())) {
            throw new MemberException(INCORRECT_PASSWORD_CHECK);
        }

        String profileImgUrl = awsS3Service.uploadFile(file, DIR);

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .gender(request.getGender())
                .profileImgUrl(profileImgUrl)
                .status(MemberStatus.ACTIVE)
                .role(MemberRole.USER)
                .build();

        memberRepository.save(member);
        return SignUpResponse.fromEntity(member);
    }


    private static Map<String, String> getMessage(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("result", message);
        return result;
    }
}
