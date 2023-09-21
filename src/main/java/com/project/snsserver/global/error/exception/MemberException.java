package com.project.snsserver.global.error.exception;

import com.project.snsserver.global.error.type.MemberErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

	private final MemberErrorCode errorCode;

	public MemberException(MemberErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
