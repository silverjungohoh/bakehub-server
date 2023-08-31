package com.project.snsserver.global.error.exception;

import com.project.snsserver.global.error.type.BoardErrorCode;
import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {

    private final BoardErrorCode errorCode;

    public BoardException(BoardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
