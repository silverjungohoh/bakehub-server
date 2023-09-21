package com.project.snsserver.global.error.handler;

import com.project.snsserver.global.error.exception.BoardException;
import com.project.snsserver.global.error.exception.ImageException;
import com.project.snsserver.global.error.exception.MemberException;
import com.project.snsserver.global.error.model.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Objects;

import static com.project.snsserver.global.error.type.ErrorCode.INVALID_ARGUMENT;
import static com.project.snsserver.global.error.type.ImageErrorCode.EXCEEDED_IMAGE_SIZE_LIMIT;

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

	@ExceptionHandler(ImageException.class)
	public ResponseEntity<ErrorResponse> handleImageException(ImageException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		ErrorResponse response = ErrorResponse.builder()
			.status(INVALID_ARGUMENT.getStatus().value())
			.code(INVALID_ARGUMENT.getCode())
			.message(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
			.build();

		return ResponseEntity.status(INVALID_ARGUMENT.getStatus()).body(response);
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
