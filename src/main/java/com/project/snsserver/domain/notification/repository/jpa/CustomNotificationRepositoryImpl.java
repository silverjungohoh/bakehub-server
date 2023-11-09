package com.project.snsserver.domain.notification.repository.jpa;

import static com.project.snsserver.domain.member.model.entity.QMember.*;
import static com.project.snsserver.domain.notification.model.entity.QNotification.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.project.snsserver.domain.notification.model.dto.NotificationResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

	private final JPAQueryFactory queryFactory;

	/**
	 * 회원의 알림 목록 조회
	 */
	@Override
	public Slice<NotificationResponse> findAllNotificationByMemberId(Long memberId, Long lastNotificationId,
		Pageable pageable) {

		List<NotificationResponse> notifications = queryFactory.select(
				fields(NotificationResponse.class,
					notification.id.as("notificationId"),
					notification.type,
					notification.content,
					notification.relatedUrl,
					notification.createdAt
				)
			)
			.from(notification)
			.innerJoin(notification.member, member)
			.where(
				lastNotificationId(lastNotificationId),
				notification.member.id.eq(memberId)
			)
			.orderBy(notification.createdAt.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return checkLastPage(pageable, notifications);
	}

	private BooleanExpression lastNotificationId(Long lastNotificationId) {
		if (lastNotificationId == null) {
			return null;
		}
		return notification.id.lt(lastNotificationId);
	}

	private Slice<NotificationResponse> checkLastPage(Pageable pageable, List<NotificationResponse> notifications) {

		boolean hasNext = false;

		if (notifications.size() > pageable.getPageSize()) {
			hasNext = true;
			notifications.remove(pageable.getPageSize());
		}
		return new SliceImpl<>(notifications, pageable, hasNext);
	}
}
