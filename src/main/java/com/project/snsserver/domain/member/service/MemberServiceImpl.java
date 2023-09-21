package com.project.snsserver.domain.member.service;

import com.project.snsserver.domain.awss3.service.AwsS3Service;
import com.project.snsserver.domain.board.repository.jpa.*;
import com.project.snsserver.domain.mail.model.MailMessage;
import com.project.snsserver.domain.mail.service.MailService;
import com.project.snsserver.domain.member.model.dto.*;
import com.project.snsserver.domain.member.model.entity.LogoutAccessToken;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.model.entity.MemberAuthCode;
import com.project.snsserver.domain.member.model.entity.RefreshToken;
import com.project.snsserver.domain.member.repository.jpa.MemberRepository;
import com.project.snsserver.domain.member.repository.redis.LogoutAccessTokenRepository;
import com.project.snsserver.domain.member.repository.redis.MemberAuthCodeRepository;
import com.project.snsserver.domain.member.repository.redis.RefreshTokenRepository;
import com.project.snsserver.domain.member.type.MemberRole;
import com.project.snsserver.domain.member.type.MemberStatus;
import com.project.snsserver.domain.notification.repository.jpa.NotificationRepository;
import com.project.snsserver.domain.security.CustomUserDetails;
import com.project.snsserver.domain.security.jwt.JwtTokenProvider;
import com.project.snsserver.global.error.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    private final LogoutAccessTokenRepository logoutAccessTokenRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostHeartRepository postHeartRepository;
    private final NotificationRepository notificationRepository;
    private final PostImageRepository postImageRepository;
    private final PostHashtagRepository postHashtagRepository;


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
    @Transactional
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
                .expiration(1000L * 60 * 3)
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
    @Transactional
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
    public ReissueTokenResponse reissueAccessToken(String refreshToken, Member member) {

        RefreshToken refreshTokenInRedis = refreshTokenRepository.findById(member.getEmail())
                .orElseThrow(() -> new MemberException(FAIL_TO_REISSUE_TOKEN));

        if(!Objects.equals(refreshTokenInRedis.getRefreshToken(), refreshToken)) {
            log.error("rtk in redis and rtk in request header are not equal");
            throw new MemberException(FAIL_TO_REISSUE_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(member.getEmail(), member.getRole().name());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        return ReissueTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    @Transactional
    public Map<String, String> logout(LogoutRequest request, String email) {

        String accessToken = request.getAccessToken();
        Long remainingTime = jwtTokenProvider.getRemainingTime(accessToken);

        LogoutAccessToken logoutAccessToken = LogoutAccessToken.builder()
                .id(accessToken)
                .email(email)
                .expiration(remainingTime)
                .build();

        logoutAccessTokenRepository.save(logoutAccessToken);

        // redis refresh token 삭제
        refreshTokenRepository.deleteById(email);

        return getMessage("로그아웃에 성공하였습니다.");
    }

    @Override
    @Transactional
    public Map<String, String> updatePassword(UpdatePasswordRequest request, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->  new MemberException(MEMBER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(INCORRECT_NOW_PASSWORD);
        }

        if(!Objects.equals(request.getNewPassword(), request.getPasswordCheck())) {
            throw new MemberException(INCORRECT_PASSWORD_CHECK);
        }

        member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        return getMessage("비밀번호 변경이 완료되었습니다.");
    }

    @Override
    @Transactional
    public Map<String, String> updateNickname(UpdateNicknameRequest request, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if(memberRepository.existsByNickname(request.getNickname())) {
            throw new MemberException(DUPLICATED_NICKNAME);
        }

        member.updateNickname(request.getNickname());
        return getMessage("닉네임 변경이 완료되었습니다.");
    }

    @Override
    @Transactional
    public Map<String, String> updateProfileImg(MultipartFile file, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        String newProfileImgUrl = awsS3Service.uploadFile(file, DIR);
        awsS3Service.deleteFile(member.getProfileImgUrl(), DIR);

        member.updateProfileImg(newProfileImgUrl);
        return getMessage("프로필 이미지 변경이 완료되었습니다.");
    }

    @Override
    @Transactional
    public Map<String, String> withdraw(WithdrawRequest request, Member member) {

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(FAIL_TO_WITHDRAWAL);
        }

        // 회원이 작성한 댓글, 좋아요 삭제
        commentRepository.deleteCommentAllByMemberId(member.getId());
        postHeartRepository.deletePostHeartAllByMemberId(member.getId());

        // 회원의 게시물에 달린 댓글, 좋아요, 해시태그 삭제
        commentRepository.deleteCommentAllInPostIdsByMemberId(member.getId());
        postHeartRepository.deletePostHeartAllInPostIdsByMemberId(member.getId());
        postHashtagRepository.deletePostHashtagAllInPostIdsByMemberId(member.getId());

        // 회원의 게시물 이미지 삭제
        postImageRepository.deleteAllPostImageInPostIdsByMemberId(member.getId());

        // 회원의 알림 전체 삭제
        notificationRepository.deleteNotificationAllByMemberId(member.getId());

        // 회원의 게시물 전체 삭제
        postRepository.deleteAllPostByMemberId(member.getId());

        awsS3Service.deleteFile(member.getProfileImgUrl(), DIR);
        memberRepository.delete(member);

        return getMessage("회원 탈퇴가 완료되었습니다.");
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailResponse getMemberDetail(Member member) {
        return memberRepository.findMemberDetailByMemberId(member.getId());
    }

    @Override
    public ProfileResponse getMemberProfile(Member member) {
        return ProfileResponse.fromEntity(member);
    }


    private static Map<String, String> getMessage(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("result", message);
        return result;
    }
}
