package com.project.snsserver.domain.notification.model.dto;

import com.project.snsserver.domain.notification.type.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {

	private String nickname; // receiver

	private NotificationType type;

	private Long targetId;
}

