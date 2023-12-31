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

	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "E101", "이미 존재하는 이메일입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "E102", "이미 존재하는 닉네임입니다."),
	FAIL_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E103", "이메일 전송에 실패하였습니다."),
	INCORRECT_EMAIL_AUTH_CODE(HttpStatus.BAD_REQUEST, "E104", "메일 인증번호가 일치하지 않습니다."),
	INCORRECT_PASSWORD_CHECK(HttpStatus.BAD_REQUEST, "E105", "비밀번호가 일치하지 않습니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "E106", "존재하지 않는 회원입니다."),
	INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "E107", "비밀번호가 올바르지 않습니다."),
	FAIL_TO_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "E108", "사용자 인증에 실패하였습니다."),
	FAIL_TO_AUTHORIZATION(HttpStatus.FORBIDDEN, "E109", "사용자 권한이 없습니다."),
	FAIL_TO_REISSUE_TOKEN(HttpStatus.UNAUTHORIZED, "E110", "token 재발급이 불가능합니다."),
	FAIL_TO_WITHDRAWAL(HttpStatus.BAD_REQUEST, "E111", "비밀번호 입력이 올바르지 않아 탈퇴가 불가능합니다."),
	INCORRECT_NOW_PASSWORD(HttpStatus.BAD_REQUEST, "E112", "기존의 비밀번호가 올바르지 않습니다."),
	ALREADY_FOLLOWING_MEMBER(HttpStatus.BAD_REQUEST, "E113", "이미 팔로잉 하고 있는 회원입니다."),
	FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "E114", "팔로우 한 회원이 아닙니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
