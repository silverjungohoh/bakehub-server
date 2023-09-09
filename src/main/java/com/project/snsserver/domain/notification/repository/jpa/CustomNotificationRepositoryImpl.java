package com.project.snsserver.domain.notification.repository.jpa;

import com.project.snsserver.domain.notification.model.dto.NotificationResponse;
import com.project.snsserver.domain.notification.model.entity.Notification;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.snsserver.domain.member.model.entity.QMember.member;
import static com.project.snsserver.domain.notification.model.entity.QNotification.notification;

@RequiredArgsConstructor
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 회원의 알림 목록 조회
     */
    @Override
    public Slice<NotificationResponse> findNotificationAllByMemberId(Long memberId, Long lastNotificationId, Pageable pageable) {

        List<Notification> notifications = queryFactory.selectFrom(notification)
                .leftJoin(notification.member, member).fetchJoin()
                .where(
                        lastNotificationId(lastNotificationId),
                        notification.member.id.eq(memberId)
                )
                .orderBy(notification.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, notifications);
    }

    @Override
    public Long deleteNotificationAllByMemberId(Long memberId) {
        return queryFactory.delete(notification)
                .where(notification.member.id.eq(memberId))
                .execute();
    }

    private BooleanExpression lastNotificationId(Long lastNotificationId) {
        if(lastNotificationId == null) {
            return null;
        }
        return notification.id.lt(lastNotificationId);
    }

    private Slice<NotificationResponse> checkLastPage(Pageable pageable, List<Notification> notifications) {

        boolean hasNext = false;

        if (notifications.size() > pageable.getPageSize()) {
            hasNext = true;
            notifications.remove(pageable.getPageSize());
        }

        List<NotificationResponse> notificationByMember
                = notifications.stream()
                .map(NotificationResponse :: fromEntity)
                .collect(Collectors.toList());

        return new SliceImpl<>(notificationByMember, pageable, hasNext);
    }
}
