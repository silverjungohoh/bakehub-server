package com.project.snsserver.domain.member.service;

import com.project.snsserver.domain.awss3.service.AwsS3Service;
import com.project.snsserver.domain.mail.model.MailMessage;
import com.project.snsserver.domain.mail.service.MailService;
import com.project.snsserver.domain.member.model.dto.*;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.model.entity.MemberAuthCode;
import com.project.snsserver.domain.member.model.entity.RefreshToken;
import com.project.snsserver.domain.member.repository.jpa.MemberRepository;
import com.project.snsserver.domain.member.repository.redis.MemberAuthCodeRepository;
import com.project.snsserver.domain.member.repository.redis.RefreshTokenRepository;
import com.project.snsserver.domain.member.type.MemberRole;
import com.project.snsserver.domain.member.type.MemberStatus;
import com.project.snsserver.domain.security.CustomUserDetails;
import com.project.snsserver.domain.security.jwt.JwtTokenProvider;
import com.project.snsserver.global.error.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


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

        boolean result = mailService.sendMail(mail, authCode);
        if(!result) throw new MemberException(FAIL_TO_SEND_EMAIL);

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

        if (!Objects.equals(request.getPasswordCheck(), request.getPassword())) {
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
    @Override
    public LoginResponse login(LoginRequest request) {

        // UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // authenticate 메서드가 실행이 될 때 loadUserByUsername 메서드가 실행
        // 성공 시 사용자 정보가 담긴 Authentication 객체를 생성하여 반환
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // SecurityContext에 Authentication 객체를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails.getUsername(), userDetails.getRole().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public ReissueTokenResponse reissueAccessToken(ReissueTokenRequest request) {
        String token = request.getRefreshToken();

        // refresh token 유효성 확인
        if(!jwtTokenProvider.validateRefreshToken(token)) {
            throw new MemberException(INVALID_REFRESH_TOKEN);
        }

        String email = jwtTokenProvider.extractUsername(token);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        RefreshToken refreshToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new MemberException(INVALID_REFRESH_TOKEN));

        if(!Objects.equals(refreshToken.getRefreshToken(), token)) {
            throw new MemberException(INVALID_REFRESH_TOKEN);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(email, member.getRole().name());

        return ReissueTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }


    private static Map<String, String> getMessage(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("result", message);
        return result;
    }
}
