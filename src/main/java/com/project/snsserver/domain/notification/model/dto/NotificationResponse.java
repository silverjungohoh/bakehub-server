package com.project.snsserver.domain.notification.model.dto;

import com.project.snsserver.domain.notification.model.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long notificationId;

    private String type;

    private String content;

    private Timestamp createdAt;


    public static NotificationResponse fromEntity(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .type(notification.getNotificationType().name())
                .content(notification.getContent())
                .createdAt(Timestamp.valueOf(notification.getCreatedAt()))
                .build();
    }
}
