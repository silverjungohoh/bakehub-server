package com.project.snsserver.global.error.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 회원에 대한 예외 CODE = E101 ~ E199
 */

@Getter
@AllArgsConstructor
public enum MemberErrorCode {

    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "E101", "이미 존재하는 이메일입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
