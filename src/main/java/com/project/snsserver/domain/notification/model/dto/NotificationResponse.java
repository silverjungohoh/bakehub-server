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

    private String type;

    private String content;

    private Timestamp createdAt; // 좋아요 or 댓글을 남긴 시간


    public static NotificationResponse fromEntity(Notification notification, Timestamp createdAt) {
        return NotificationResponse.builder()
                .type(notification.getNotificationType().name())
                .content(notification.getContent())
                .createdAt(createdAt)
                .build();
    }
}
