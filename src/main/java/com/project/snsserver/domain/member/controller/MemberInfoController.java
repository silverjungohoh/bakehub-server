package com.project.snsserver.domain.member.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.snsserver.domain.member.model.dto.request.UpdateNicknameRequest;
import com.project.snsserver.domain.member.model.dto.request.UpdatePasswordRequest;
import com.project.snsserver.domain.member.model.dto.request.WithdrawRequest;
import com.project.snsserver.domain.member.model.dto.response.MemberDetailResponse;
import com.project.snsserver.domain.member.model.dto.response.MyDetailResponse;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.service.MemberInfoService;
import com.project.snsserver.domain.security.CustomUserDetails;
import com.project.snsserver.global.util.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "회원 정보", description = "회원 정보 API Document")
public class MemberInfoController {

	private final MemberInfoService memberInfoService;

	/**
	 * 회원 비밀번호 변경
	 */
	@Operation(summary = "회원 비밀번호 변경")
	@PatchMapping("/info/password")
	public ResponseEntity<Map<String, String>> updatePassword(@RequestBody @Valid UpdatePasswordRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		Map<String, String> response = memberInfoService.updatePassword(request, userDetails.getUsername());
		return ResponseEntity.ok(response);
	}

	/**
	 * 회원 닉네임 변경
	 */
	@Operation(summary = "회원 닉네임 변경")
	@PatchMapping("/info/nickname")
	public ResponseEntity<Map<String, String>> updateNickname(@RequestBody @Valid UpdateNicknameRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		Map<String, String> response = memberInfoService.updateNickname(request, userDetails.getUsername());
		return ResponseEntity.ok(response);
	}

	/**
	 * 회원 프로필 이미지 변경
	 */
	@Operation(summary = "회원 프로필 이미지 변경")
	@PatchMapping("/info/profile")
	public ResponseEntity<Map<String, String>> updateProfileImg(@RequestPart(value = "image") MultipartFile file,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Map<String, String> response = memberInfoService.updateProfileImg(file, userDetails.getUsername());
		return ResponseEntity.ok(response);
	}

	/**
	 * 회원 탈퇴
	 */
	@Operation(summary = "회원 탈퇴")
	@DeleteMapping("/info")
	public ResponseEntity<Map<String, String>> withdraw(@RequestBody @Valid WithdrawRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		Map<String, String> response = memberInfoService.withdraw(request, userDetails.getUsername());
		return ResponseEntity.ok(response);
	}

	/**
	 * 회원 정보 조회
	 */
	@Operation(summary = "나의 회원 상세 정보 조회")
	@GetMapping("/info")
	public ResponseEntity<MyDetailResponse> getMyMemberDetail(@AuthMember Member member) {

		MyDetailResponse response = memberInfoService.getMyDetail(member);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "다른 회원 상세 정보 조회")
	@GetMapping("/{memberId}/info")
	public ResponseEntity<MemberDetailResponse> getMemberDetail(@AuthMember Member member,
		@PathVariable Long memberId) {

		MemberDetailResponse response = memberInfoService.getMemberDetail(member.getId(), memberId);
		return ResponseEntity.ok(response);
	}

}
