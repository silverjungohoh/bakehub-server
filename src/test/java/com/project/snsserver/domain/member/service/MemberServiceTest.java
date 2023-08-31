package com.project.snsserver.domain.member.service;

import com.project.snsserver.domain.awss3.service.AwsS3Service;
import com.project.snsserver.domain.mail.service.MailService;
import com.project.snsserver.domain.member.model.dto.SignUpRequest;
import com.project.snsserver.domain.member.model.dto.SignUpResponse;
import com.project.snsserver.domain.member.model.dto.VerifyAuthCodeRequest;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.model.entity.MemberAuthCode;
import com.project.snsserver.domain.member.repository.jpa.MemberRepository;
import com.project.snsserver.domain.member.repository.redis.MemberAuthCodeRepository;
import com.project.snsserver.domain.member.type.Gender;
import com.project.snsserver.global.error.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static com.project.snsserver.global.error.type.MemberErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberAuthCodeRepository memberAuthCodeRepository;

    @Mock
    private AwsS3Service awsS3Service;

    @Mock
    private MailService mailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("이메일 중복 확인 - 성공")
    void checkEmail_Success() {
        String email = "test@test.com";
        // given
        given(memberRepository.existsByEmail(anyString())).willReturn(false);
        // when
        Map<String, String> result = memberService.checkEmailDuplicate(email);
        // then
        assertNotNull(result.get("result"));
    }

    @Test
    @DisplayName("이메일 중복 확인 - 실패")
    void checkEmail_Fail() {
        String email = "test@test.com";
        // given
        given(memberRepository.existsByEmail(anyString())).willReturn(true);
        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.checkEmailDuplicate(email));
        // then
        assertEquals(exception.getErrorCode(), DUPLICATED_EMAIL);
        assertEquals(exception.getMessage(), DUPLICATED_EMAIL.getMessage());
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 성공")
    void checkNickname_Success() {
        String nick = "nick";
        // given
        given(memberRepository.existsByNickname(anyString())).willReturn(false);
        // when
        Map<String, String> result = memberService.checkNicknameDuplicate(nick);
        // then
        assertNotNull(result.get("result"));
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 실패")
    void checkNickname_Fail() {
        String nick = "nick";
        // given
        given(memberRepository.existsByNickname(anyString())).willReturn(true);
        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.checkNicknameDuplicate(nick));
        // then
        assertEquals(exception.getErrorCode(), DUPLICATED_NICKNAME);
        assertEquals(exception.getMessage(), DUPLICATED_NICKNAME.getMessage());
    }


    @Test
    @DisplayName("이메일 전송 - 성공")
    void sendEmail_Success() {
        String email = "test@test.com";
        // given
        given(mailService.sendMail(any(), anyString())).willReturn(true);

        ArgumentCaptor<MemberAuthCode> captor = ArgumentCaptor.forClass(MemberAuthCode.class);

        // when
        Map<String, String> result = memberService.sendEmailAuthCode(email);

        // then
        assertNotNull(result.get("result"));
        verify(memberAuthCodeRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("이메일 전송 - 실패")
    void sendEmail_Fail() {
        String email = "test@test.com";

        // given
        given(mailService.sendMail(any(), anyString())).willReturn(false);
        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.sendEmailAuthCode(email));

        // then
        assertEquals(exception.getErrorCode(), FAIL_TO_SEND_EMAIL);
        assertEquals(exception.getMessage(), FAIL_TO_SEND_EMAIL.getMessage());
    }

    @Test
    @DisplayName("이메일 인증 - 성공")
    void verifyEmail_Success() {
        VerifyAuthCodeRequest request = VerifyAuthCodeRequest.builder()
                .email("test@test.com")
                .code("a1b2c3d4e5f6")
                .build();

        MemberAuthCode authCode = MemberAuthCode.builder()
                .email(request.getEmail())
                .id(request.getCode())
                .expiration(100L)
                .build();

        // given
        given(memberAuthCodeRepository.findById(anyString())).willReturn(Optional.of(authCode));
        // when
        Map<String, String> result = memberService.verifyEmailAuthCode(request);
        // then
        assertNotNull(result.get("result"));
    }

    @Test
    @DisplayName("이메일 인증 - 실패1")
    void verifyEmail_Fail1() {
        VerifyAuthCodeRequest request = VerifyAuthCodeRequest.builder()
                .email("test@test.com")
                .code("a1b2c3d4e5f6")
                .build();

        // given
        given(memberAuthCodeRepository.findById(anyString())).willReturn(Optional.empty());
        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.verifyEmailAuthCode(request));
        // then
        assertEquals(exception.getErrorCode(), INCORRECT_EMAIL_AUTH_CODE);
        assertEquals(exception.getMessage(), INCORRECT_EMAIL_AUTH_CODE.getMessage());
    }

    @Test
    @DisplayName("이메일 인증 - 실패2")
    void verifyEmail_Fail2() {
        VerifyAuthCodeRequest request = VerifyAuthCodeRequest.builder()
                .email("test111@test.com")
                .code("a1b2c3d4e5f6")
                .build();

        MemberAuthCode authCode = MemberAuthCode.builder()
                .email("test123@test.com")
                .id(request.getCode())
                .expiration(100L)
                .build();

        // given
        given(memberAuthCodeRepository.findById(anyString())).willReturn(Optional.of(authCode));
        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.verifyEmailAuthCode(request));
        // then
        assertEquals(exception.getErrorCode(), INCORRECT_EMAIL_AUTH_CODE);
        assertEquals(exception.getMessage(), INCORRECT_EMAIL_AUTH_CODE.getMessage());
    }

    @Test
    @DisplayName("회원 가입 - 성공")
    void signUp_Success() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@test.com")
                .nickname("nickname")
                .password("test1234")
                .passwordCheck("test1234")
                .gender(Gender.FEMALE)
                .build();

        MockMultipartFile file
                = new MockMultipartFile("test", "test_2023.png", "image/png", "test".getBytes());

        given(awsS3Service.uploadFile(any(), anyString())).willReturn(file.getName());
        given(passwordEncoder.encode(request.getPassword())).willReturn("password-encryption");

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        SignUpResponse response = memberService.signUp(file, request);

        assertEquals(response.getNickname(), request.getNickname());
        assertEquals(response.getProfileImgUrl(), file.getName());
        verify(memberRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("회원 가입 - 실패")
    void signUp_Fail() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@test.com")
                .nickname("nickname")
                .password("test1234")
                .passwordCheck("test4321")
                .gender(Gender.FEMALE)
                .build();

        MockMultipartFile file
                = new MockMultipartFile("test", "test_2023.png", "image/png", "test".getBytes());

        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.signUp(file, request));

        assertEquals(exception.getErrorCode(), INCORRECT_PASSWORD_CHECK);
        assertEquals(exception.getMessage(), INCORRECT_PASSWORD_CHECK.getMessage());
    }
}