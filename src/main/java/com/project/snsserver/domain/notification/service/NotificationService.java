package com.project.snsserver.domain.notification.service;

import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.notification.model.dto.NotificationMessage;
import com.project.snsserver.domain.notification.model.dto.NotificationResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationService {

	void send(NotificationMessage message);

	/**
	 * 회원의 알림 목록 조회
	 */
	Slice<NotificationResponse> getMyNotificationList(Pageable pageable, Member member, Long lastNotificationId);
}
