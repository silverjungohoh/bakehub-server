package com.project.snsserver.domain.member.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.snsserver.domain.member.model.dto.request.LoginRequest;
import com.project.snsserver.domain.member.model.dto.request.LogoutRequest;
import com.project.snsserver.domain.member.model.dto.request.SendAuthCodeRequest;
import com.project.snsserver.domain.member.model.dto.request.SignUpRequest;
import com.project.snsserver.domain.member.model.dto.request.VerifyAuthCodeRequest;
import com.project.snsserver.domain.member.model.dto.response.LoginResponse;
import com.project.snsserver.domain.member.model.dto.response.ProfileResponse;
import com.project.snsserver.domain.member.model.dto.response.ReissueTokenResponse;
import com.project.snsserver.domain.member.model.dto.response.SignUpResponse;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.service.MemberService;
import com.project.snsserver.global.util.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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
	public ResponseEntity<SignUpResponse> signUp(@RequestPart(value = "image") MultipartFile file,
		@RequestPart(value = "data") @Valid SignUpRequest request) {

		SignUpResponse response = memberService.signUp(file, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 회원 로그인
	 */
	@Operation(summary = "회원 로그인")
	@PostMapping("/auth/login")
	public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {

		LoginResponse response = memberService.login(request);
		return ResponseEntity.ok(response);
	}

	/**
	 * token 재발급
	 */
	@Operation(summary = "회원 token 재발급")
	@PostMapping("/auth/token")
	public ResponseEntity<ReissueTokenResponse> reissueAccessToken(@RequestHeader("RTK") String refreshToken,
		@AuthMember Member member) {

		ReissueTokenResponse response = memberService.reissueAccessToken(refreshToken, member);
		return ResponseEntity.ok(response);
	}

	/**
	 * 회원 로그아웃
	 */
	@Operation(summary = "회원 로그아웃")
	@PostMapping("/auth/logout")
	public ResponseEntity<Map<String, String>> logout(@RequestBody LogoutRequest request, @AuthMember Member member) {

		Map<String, String> response = memberService.logout(request, member.getEmail());
		return ResponseEntity.ok(response);
	}

	/**
	 * 회원 프로필 조회
	 */
	@Operation(summary = "회원 프로필 조회")
	@GetMapping("/profile")
	public ResponseEntity<ProfileResponse> getMemberProfile(@AuthMember Member member) {

		ProfileResponse response = memberService.getMemberProfile(member);
		return ResponseEntity.ok(response);
	}
}
