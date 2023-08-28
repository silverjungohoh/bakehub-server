package com.project.snsserver.global.error.handler;

import com.project.snsserver.global.error.exception.ImageException;
import com.project.snsserver.global.error.exception.MemberException;
import com.project.snsserver.global.error.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse<String>> handleMemberException(MemberException e) {

        ErrorResponse<String> response = ErrorResponse.<String>builder()
                .status(e.getErrorCode().getStatus().value())
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .build();

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ErrorResponse<String>> handleImageException(ImageException e) {

        ErrorResponse<String> response = ErrorResponse.<String>builder()
                .status(e.getErrorCode().getStatus().value())
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .build();

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }
}
