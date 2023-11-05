package com.project.snsserver.domain.notification.sse.repository;

public interface ConnectionRepository<T, R> {

	void add(T uniqueKey, R connection);

	R get(T uniqueKey);

	void delete(R connection);
}
