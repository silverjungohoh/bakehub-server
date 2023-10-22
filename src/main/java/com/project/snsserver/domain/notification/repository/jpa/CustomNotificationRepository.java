package com.project.snsserver.domain.notification.repository.jpa;

import com.project.snsserver.domain.notification.model.dto.NotificationResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomNotificationRepository {

	Slice<NotificationResponse> findAllNotificationByMemberId(Long memberId, Long lastNotificationId,
		Pageable pageable);

	Long deleteAllNotificationByMemberId(Long memberId);
}
