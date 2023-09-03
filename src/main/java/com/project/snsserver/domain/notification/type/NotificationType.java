package com.project.snsserver.domain.notification.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    NEW_COMMENT("%s님이 회원님의 게시물에 댓글을 남겼습니다!"),
    NEW_HEART("%s님이 회원님의 게시물을 좋아합니다!");

    private final String value;
}
