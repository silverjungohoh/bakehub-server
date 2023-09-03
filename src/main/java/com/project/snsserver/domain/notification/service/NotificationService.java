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
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.project.snsserver.global.error.type.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final String NAME = "notification";
    private final SseConnectionPool sseConnectionPool;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    public void send(NotificationMessage message) {

        // save notification
        String receiver = message.getReceiver();

        Member member = memberRepository.findByEmail(receiver)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Notification notification = Notification.builder()
                .notificationType(message.getType())
                .content(message.getContent())
                .member(member)
                .build();

        notificationRepository.save(notification);

        var sseConnection
                = sseConnectionPool.get(receiver);

        NotificationResponse response
                = NotificationResponse.fromEntity(notification, message.getCreatedAt());

        Optional.ofNullable(sseConnection)
                .ifPresent((it) -> it.sendMessage(NAME, response));
    }
}