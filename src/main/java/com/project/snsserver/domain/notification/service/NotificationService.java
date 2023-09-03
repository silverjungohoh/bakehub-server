package com.project.snsserver.domain.notification.service;

import com.project.snsserver.domain.notification.model.dto.NotificationMessage;

public interface NotificationService {

    void send(NotificationMessage message);
}
