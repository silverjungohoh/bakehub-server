package com.project.snsserver.global.error.exception;

import com.project.snsserver.global.error.type.CommonErrorCode;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

	private final CommonErrorCode errorCode;

	public CommonException(CommonErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
