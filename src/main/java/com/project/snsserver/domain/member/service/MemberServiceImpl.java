package com.project.snsserver.domain.member.service;

import com.project.snsserver.domain.mail.model.MailMessage;
import com.project.snsserver.domain.mail.service.MailService;
import com.project.snsserver.domain.member.model.entity.MemberAuthCode;
import com.project.snsserver.domain.member.repository.MemberRepository;
import com.project.snsserver.domain.member.repository.redis.MemberAuthCodeRepository;
import com.project.snsserver.global.error.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.project.snsserver.global.error.type.MemberErrorCode.DUPLICATED_EMAIL;
import static com.project.snsserver.global.error.type.MemberErrorCode.DUPLICATED_NICKNAME;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final String SUBJECT = "[회원 인증] 이메일 인증 코드 발송 안내";

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final MemberAuthCodeRepository memberAuthCodeRepository;


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


    private static Map<String, String> getMessage(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("result", message);
        return result;
    }
}
