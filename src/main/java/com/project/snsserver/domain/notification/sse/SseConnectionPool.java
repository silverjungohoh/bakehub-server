package com.project.snsserver.domain.notification.sse;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseConnectionPool implements ConnectionPool<String, SseConnection> {

	private static final Map<String, SseConnection> connectionPool = new ConcurrentHashMap<>();

	@Override
	public void add(String uniqueKey, SseConnection sseConnection) {
		connectionPool.put(uniqueKey, sseConnection);
	}

	@Override
	public SseConnection get(String uniqueKey) {
		return connectionPool.get(uniqueKey);
	}

	@Override
	public void delete(SseConnection sseConnection) {
		connectionPool.remove(sseConnection.getUniqueKey());
	}
}
