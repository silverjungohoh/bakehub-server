package com.project.snsserver.domain.notification.service;

import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.repository.jpa.MemberRepository;
import com.project.snsserver.domain.notification.model.dto.NotificationMessage;
import com.project.snsserver.domain.notification.model.dto.NotificationResponse;
import com.project.snsserver.domain.notification.model.entity.Notification;
import com.project.snsserver.domain.notification.repository.jpa.NotificationRepository;
import com.project.snsserver.domain.notification.sse.SseConnectionPool;
import com.project.snsserver.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.project.snsserver.global.error.type.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private static final String NAME = "notification";
	private final SseConnectionPool sseConnectionPool;
	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	@Override
	public void send(NotificationMessage message) {

		// save notification
		String nickname = message.getNickname();

		Member receiver = memberRepository.findByNickname(nickname)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Notification notification = Notification.builder()
			.type(message.getType())
			.content(String.format(message.getType().getText(), nickname))
			.relatedUrl(String.format("/post/%d", message.getTargetId()))
			.member(receiver)
			.build();

		notificationRepository.save(notification);

		var sseConnection
			= sseConnectionPool.get(nickname);

		NotificationResponse response
			= NotificationResponse.fromEntity(notification);

		Optional.ofNullable(sseConnection)
			.ifPresent((it) -> it.sendMessage(NAME, response));
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<NotificationResponse> getMyNotificationList(Pageable pageable, Member member,
		Long lastNotificationId) {
		return notificationRepository.findAllNotificationByMemberId(member.getId(), lastNotificationId, pageable);
	}
}
