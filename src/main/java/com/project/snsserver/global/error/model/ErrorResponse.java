package com.project.snsserver.global.error.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse<T> {

    private int status;

    private String code;

    private T message;
}
