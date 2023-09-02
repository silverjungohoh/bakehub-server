package com.project.snsserver.domain.notification.model.dto;

import com.project.snsserver.domain.notification.type.NotificationType;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {

    private String receiver;

    private NotificationType type;

    private String content;

    private Timestamp createdAt;
}

