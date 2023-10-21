package com.project.snsserver.global.error.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

/**
 * 공통 에러 E001 ~ E099
 */

@Getter
@AllArgsConstructor
public enum CommonErrorCode {

	INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "E001", "유효성 검증 실패"),
	FAIL_TO_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "E002", "이미지 업로드에 실패하였습니다"),
	INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "E003", "유효하지 않은 이미지 형식입니다."),
	EXCEEDED_IMAGE_SIZE_LIMIT(HttpStatus.PAYLOAD_TOO_LARGE, "E004", "업로드 가능한 이미지 크기를 초과하였습니다."),
	FAIL_TO_DELETE_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "E005", "이미지 삭제에 실패하였습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
