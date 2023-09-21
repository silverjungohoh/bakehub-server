package com.project.snsserver.global.error.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

/**
 * 공통 에러 E001 ~ E099
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {

	INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "E001", "유효성 검증 실패");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
