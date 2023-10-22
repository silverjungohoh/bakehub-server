package com.project.snsserver.domain.notification.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.notification.model.dto.NotificationResponse;
import com.project.snsserver.domain.notification.service.NotificationService;
import com.project.snsserver.domain.notification.sse.SseConnection;
import com.project.snsserver.domain.notification.sse.SseConnectionPool;
import com.project.snsserver.global.util.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "알림", description = "알림 API Document")
public class NotificationController {

	private final SseConnectionPool sseConnectionPool;
	private final ObjectMapper objectMapper;
	private final NotificationService notificationService;

	@Operation(summary = "server sent events 연결")
	@GetMapping(path = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseBodyEmitter connect(@AuthMember Member member) {

		var sseConnection
			= SseConnection.connect(member.getEmail(), sseConnectionPool, objectMapper);

		sseConnectionPool.add(sseConnection.getUniqueKey(), sseConnection);
		return sseConnection.getSseEmitter();
	}

	/**
	 * 회원의 알림 목록 조회
	 */
	@Operation(summary = "회원의 알림 목록 조회")
	@GetMapping("/notifications")
	public ResponseEntity<Slice<NotificationResponse>> getNotificationsByMember(@PageableDefault Pageable pageable,
		@RequestParam(required = false) Long lastNotificationId, @AuthMember Member member) {

		Slice<NotificationResponse> response
			= notificationService.getMyNotificationList(pageable, member, lastNotificationId);
		return ResponseEntity.ok(response);
	}
}
