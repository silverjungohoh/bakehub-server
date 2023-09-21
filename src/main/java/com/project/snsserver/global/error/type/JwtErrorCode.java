package com.project.snsserver.global.error.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

/**
 * jwt 관련된 예외 CODE = E301 ~ E399
 */

@Getter
@AllArgsConstructor
public enum JwtErrorCode {

	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "E301", "만료된 token 입니다."),

	INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "E302", "유효하지 않은 token 서명입니다."),

	UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "E303", "지원되지 않는 token 입니다."),

	INCORRECT_TOKEN(HttpStatus.UNAUTHORIZED, "E304", "잘못된 token 입니다."),

	ALREADY_LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED, "E305", "이미 로그아웃한 회원입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
