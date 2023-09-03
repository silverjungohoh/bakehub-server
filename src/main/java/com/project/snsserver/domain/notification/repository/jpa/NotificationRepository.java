package com.project.snsserver.domain.notification.repository.jpa;

import com.project.snsserver.domain.notification.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
