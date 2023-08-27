package com.project.snsserver.global.error.exception;

import com.project.snsserver.global.error.type.ImageErrorCode;
import lombok.Getter;

@Getter
public class ImageException extends RuntimeException {

    private final ImageErrorCode errorCode;

    public ImageException(ImageErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
