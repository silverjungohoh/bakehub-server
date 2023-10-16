package com.project.snsserver.domain.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.snsserver.domain.member.service.FollowService;
import com.project.snsserver.domain.security.CustomUserDetails;

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

	@PostMapping("/{nickname}/follow")
	public ResponseEntity<Map<String, String>> follow(@PathVariable String nickname, @AuthenticationPrincipal
	CustomUserDetails userDetails) {

		Map<String, String> response = followService.follow(nickname, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
