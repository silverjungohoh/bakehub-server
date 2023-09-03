package com.project.snsserver.domain.notification.sse;

public interface ConnectionPool<T, R> {

    void add(T uniqueKey, R connection);

    R get(T uniqueKey);

    void delete(R connection);
}
