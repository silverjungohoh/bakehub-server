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
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
