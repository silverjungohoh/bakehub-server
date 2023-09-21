package com.project.snsserver.domain.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.snsserver.global.error.exception.CustomJwtException;
import com.project.snsserver.global.error.model.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * jwt 관련 예외 처리
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (CustomJwtException e) {
			setErrorResponse(response, e);
		}
	}

	private void setErrorResponse(HttpServletResponse response, CustomJwtException e) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(e.getErrorCode().getStatus().value());

		ErrorResponse errorResponse = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus().value())
			.code(e.getErrorCode().getCode())
			.message(e.getMessage())
			.build();

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
