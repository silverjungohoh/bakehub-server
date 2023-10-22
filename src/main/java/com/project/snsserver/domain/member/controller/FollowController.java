package com.project.snsserver.domain.member.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.snsserver.domain.member.model.dto.FollowResponse;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.service.FollowService;
import com.project.snsserver.global.util.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "팔로우", description = "팔로우 API Document")
public class FollowController {

	private final FollowService followService;

	@Operation(summary = "회원 팔로우")
	@PostMapping("/{nickname}/follow")
	public ResponseEntity<Map<String, String>> follow(@PathVariable String nickname, @AuthMember Member member) {

		Map<String, String> response = followService.follow(nickname, member);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "회원 팔로우 취소")
	@DeleteMapping("/{nickname}/follow")
	public ResponseEntity<Void> unfollow(@PathVariable String nickname, @AuthMember Member member) {

		followService.unfollow(nickname, member);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "나의 팔로잉 목록 조회")
	@GetMapping("/following")
	public ResponseEntity<Slice<FollowResponse>> getMyFollowingList(@PageableDefault Pageable pageable,
		@AuthMember Member member, @RequestParam(required = false) Long lastFollowId) {

		Slice<FollowResponse> response = followService.getMyFollowingList(member, lastFollowId, pageable);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "나의 팔로워 목록 조회")
	@GetMapping("/follower")
	public ResponseEntity<Slice<FollowResponse>> getMyFollowerList(@PageableDefault Pageable pageable,
		@AuthMember Member member, @RequestParam(required = false) Long lastFollowId) {

		Slice<FollowResponse> response = followService.getMyFollowerList(member, lastFollowId, pageable);
		return ResponseEntity.ok(response);
	}
}
