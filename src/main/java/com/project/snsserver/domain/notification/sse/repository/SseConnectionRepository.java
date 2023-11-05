package com.project.snsserver.domain.notification.sse.repository;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.project.snsserver.domain.notification.sse.SseConnection;

@Component
public class SseConnectionRepository implements ConnectionRepository<String, SseConnection> {

	private static final Map<String, SseConnection> connections = new ConcurrentHashMap<>();

	@Override
	public void add(String uniqueKey, SseConnection sseConnection) {
		connections.put(uniqueKey, sseConnection);
	}

	@Override
	public SseConnection get(String uniqueKey) {
		return connections.get(uniqueKey);
	}

	@Override
	public void delete(SseConnection sseConnection) {
		connections.remove(sseConnection.getUniqueKey());
	}
}
