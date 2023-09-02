package com.project.snsserver.domain.notification.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Getter
public class SseConnection {

    private final String uniqueKey;

    private final SseEmitter sseEmitter;

    private final ConnectionPool<String, SseConnection> connectionPool;

    private final ObjectMapper objectMapper;

    private SseConnection(
            String uniqueKey,
            ConnectionPool<String, SseConnection> connectionPool,
            ObjectMapper objectMapper
    ) {
        this.uniqueKey = uniqueKey;
        this.sseEmitter = new SseEmitter(1000L * 60 * 60);
        this.connectionPool = connectionPool;
        this.objectMapper = objectMapper;

        // on timeout
        this.sseEmitter.onTimeout(() -> {
            // 클라이언트와 타임아웃 발생 시 실행
            log.info("on timeout callback");
            this.sseEmitter.complete();
            this.connectionPool.delete(this);
        });

        // on completion
        this.sseEmitter.onCompletion(() -> {
            // 클라이언트와 연결 종료 시 실행
            log.info("on completion callback");
            this.connectionPool.delete(this);
        });

        // on open
        this.sendMessage("INIT", "connect success");
    }

    public static SseConnection connect(
            String uniqueKey,
            ConnectionPool<String, SseConnection> connectionPool,
            ObjectMapper objectMapper
    ){
        return new SseConnection(uniqueKey, connectionPool, objectMapper);
    }

    public void sendMessage(String eventName, Object data) {
        try {

            var json = this.objectMapper.writeValueAsString(data);
            var event = SseEmitter
                    .event()
                    .name(eventName)
                    .data(json);

            this.sseEmitter.send(event);

        } catch (IOException e) {
            log.error(e.getMessage());
            this.sseEmitter.completeWithError(e);
        }
    }
}
