package com.project.snsserver.global.error.handler;

import static com.project.snsserver.global.error.type.CommonErrorCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.project.snsserver.global.error.exception.BoardException;
import com.project.snsserver.global.error.exception.CommonException;
import com.project.snsserver.global.error.exception.MemberException;
import com.project.snsserver.global.error.model.ErrorResponse;
import com.project.snsserver.global.error.model.ValidationErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}

	@ExceptionHandler(CommonException.class)
	public ResponseEntity<ErrorResponse> handleCommonException(CommonException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {

		ValidationErrorResponse response = ValidationErrorResponse.from(e.getFieldErrors());

		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException() {

		ErrorResponse response = ErrorResponse.builder()
			.status(EXCEEDED_IMAGE_SIZE_LIMIT.getStatus().value())
			.code(EXCEEDED_IMAGE_SIZE_LIMIT.getCode())
			.message(EXCEEDED_IMAGE_SIZE_LIMIT.getMessage())
			.build();

		return ResponseEntity.status(EXCEEDED_IMAGE_SIZE_LIMIT.getStatus()).body(response);
	}

	@ExceptionHandler(BoardException.class)
	public ResponseEntity<ErrorResponse> handleBoardException(BoardException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}
}
