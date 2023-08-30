package com.project.snsserver.global.error.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 게시판에 대한 예외 CODE = E401 ~ E499
 */

@Getter
@AllArgsConstructor
public enum BoardErrorCode {

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
