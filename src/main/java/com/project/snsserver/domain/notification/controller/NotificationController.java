package com.project.snsserver.domain.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.snsserver.domain.notification.sse.SseConnection;
import com.project.snsserver.domain.notification.sse.SseConnectionPool;
import com.project.snsserver.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;


@Slf4j
@RestController
@RequestMapping("/api/v1/sse")
@RequiredArgsConstructor
public class NotificationController {

    private final SseConnectionPool sseConnectionPool;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter connect(@AuthenticationPrincipal CustomUserDetails userDetails) {

        var sseConnection
                = SseConnection.connect(userDetails.getUsername(), sseConnectionPool, objectMapper);

        sseConnectionPool.add(sseConnection.getUniqueKey(), sseConnection);
        return sseConnection.getSseEmitter();
    }
}
