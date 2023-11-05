package com.project.snsserver.domain.notification.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.snsserver.domain.notification.sse.repository.ConnectionRepository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Getter
public class SseConnection {

	private final String uniqueKey;

	private final SseEmitter sseEmitter;

	private final ConnectionRepository<String, SseConnection> connectionRepository;

	private final ObjectMapper objectMapper;

	private SseConnection(
		String uniqueKey,
		ConnectionRepository<String, SseConnection> connectionRepository,
		ObjectMapper objectMapper
	) {
		this.uniqueKey = uniqueKey;
		this.sseEmitter = new SseEmitter(1000L * 60 * 60);
		this.connectionRepository = connectionRepository;
		this.objectMapper = objectMapper;

		// on timeout
		this.sseEmitter.onTimeout(() -> {
			// 클라이언트와 타임아웃 발생 시 실행
			log.info("on timeout callback");
			this.sseEmitter.complete();
			this.connectionRepository.delete(this);
		});

		// on completion
		this.sseEmitter.onCompletion(() -> {
			// 클라이언트와 연결 종료 시 실행
			log.info("on completion callback");
			this.connectionRepository.delete(this);
		});

		// on open
		this.sendMessage("INIT", "connect success");
	}

	public static SseConnection connect(
		String uniqueKey,
		ConnectionRepository<String, SseConnection> connectionRepository,
		ObjectMapper objectMapper
	) {
		return new SseConnection(uniqueKey, connectionRepository, objectMapper);
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
