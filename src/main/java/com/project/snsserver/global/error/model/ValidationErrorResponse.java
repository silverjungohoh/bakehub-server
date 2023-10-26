package com.project.snsserver.global.error.model;

import static com.project.snsserver.global.error.type.CommonErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ValidationErrorResponse {

	private int status;

	private String code;

	private List<Field> message;

	public static ValidationErrorResponse from(List<FieldError> errors) {
		return ValidationErrorResponse.builder()
			.status(INVALID_ARGUMENT.getStatus().value())
			.code(INVALID_ARGUMENT.getCode())
			.message(errors.stream()
				.map(Field::from)
				.collect(Collectors.toList()))
			.build();
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Field {

		private String name;

		private String reason;

		public static Field from(FieldError error) {
			return Field.builder()
				.name(error.getField())
				.reason(error.getDefaultMessage())
				.build();
		}
	}
}
