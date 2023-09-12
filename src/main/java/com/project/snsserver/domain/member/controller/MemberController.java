package com.project.snsserver.domain.member.controller;

import com.project.snsserver.domain.member.model.dto.*;
import com.project.snsserver.domain.member.service.MemberService;
import com.project.snsserver.domain.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 API Document")
public class MemberController {

    private final MemberService memberService;

    /**
     * 이메일 중복 확인
     */
    @Operation(summary = "이메일 중복 확인")
    @GetMapping("/duplicate/email/{email}")
    public ResponseEntity<Map<String, String>> checkEmailDuplicate(@PathVariable String email) {

        Map<String, String> response = memberService.checkEmailDuplicate(email);
        return ResponseEntity.ok(response);
    }

    /**
     * 닉네임 중복 확인
     */
    @Operation(summary = "닉네임 중복 확인")
    @GetMapping("/duplicate/nickname/{nickname}")
    public ResponseEntity<Map<String, String>> checkNicknameDuplicate(@PathVariable String nickname) {

        Map<String, String> response = memberService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 인증번호 발송
     */
    @Operation(summary = "이메일 인증번호 발송")
    @PostMapping("/send/email")
    public ResponseEntity<Map<String, String>> sendEmailAuthCode(@RequestBody SendAuthCodeRequest request) {

        Map<String, String> response = memberService.sendEmailAuthCode(request.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 인증번호 확인
     */
    @Operation(summary = "이메일 인증번호 확인")
    @PostMapping("/verify/email")
    public ResponseEntity<Map<String, String>> verifyEmailAuthCode(@RequestBody VerifyAuthCodeRequest request) {

        Map<String, String> response = memberService.verifyEmailAuthCode(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 가입
     */
    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp (@RequestPart(value = "image") MultipartFile file,
                                                  @RequestPart(value = "data") @Valid SignUpRequest request) {

        SignUpResponse response = memberService.signUp(file, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 회원 로그인
     */
    @Operation(summary = "회원 로그인")
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login (@RequestBody @Valid LoginRequest request) {

        LoginResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * access token 재발급
     */
    @Operation(summary = "회원 access token 재발급")
    @PostMapping("/auth/token")
    public ResponseEntity<ReissueTokenResponse> reissueAccessToken(@RequestBody ReissueTokenRequest request) {

        ReissueTokenResponse response = memberService.reissueAccessToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 로그아웃
     */
    @Operation(summary = "회원 로그아웃")
    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody LogoutRequest request,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, String> response = memberService.logout(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 비밀번호 변경
     */
    @Operation(summary = "회원 비밀번호 변경")
    @PatchMapping("/info/password")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody @Valid UpdatePasswordRequest request,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, String> response = memberService.updatePassword(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 닉네임 변경
     */
    @Operation(summary = "회원 닉네임 변경")
    @PatchMapping("/info/nickname")
    public ResponseEntity<Map<String, String>> updateNickname(@RequestBody @Valid UpdateNicknameRequest request,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, String> response = memberService.updateNickname(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 프로필 이미지 변경
     */
    @Operation(summary = "회원 프로필 이미지 변경")
    @PatchMapping("/info/profile")
    public ResponseEntity<Map<String, String>> updateProfileImg(@RequestPart(value = "image") MultipartFile file,

                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, String> response = memberService.updateProfileImg(file, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 탈퇴
     */
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/info")
    public ResponseEntity<Map<String, String>> withdraw(@RequestBody @Valid WithdrawRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, String> response = memberService.withdraw(request, userDetails.getMember());
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 조회
     */
    @Operation(summary = "회원 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<MemberDetailResponse> getMemberDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberDetailResponse response = memberService.getMemberDetail(userDetails.getMember());
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 프로필 조회
     */
    @Operation(summary = "회원 프로필 조회")
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getMemberProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {

        ProfileResponse response = memberService.getMemberProfile(userDetails.getMember());
        return ResponseEntity.ok(response);
    }
}
