package com.project.snsserver.domain.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.snsserver.global.error.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.project.snsserver.global.error.type.MemberErrorCode.FAIL_TO_AUTHENTICATION;

/**
 * AuthenticationException 처리
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error(authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ErrorResponse<String> errorResponse = ErrorResponse.<String>builder()
                .status(FAIL_TO_AUTHENTICATION.getStatus().value())
                .code(FAIL_TO_AUTHENTICATION.getCode())
                .message(FAIL_TO_AUTHENTICATION.getMessage())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
