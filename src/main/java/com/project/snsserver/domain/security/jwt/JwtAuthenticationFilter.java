package com.project.snsserver.domain.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * access token 유효한지 검증
 * 검증 성공 시 Security Context에 Authentication 객체 저장
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] EXCLUDED_URL
            = {"/api/v1/members/sign-up", "/api/v1/members/auth/login", "/api/v1/members/auth/token",
            "/api/v1/members/duplicate/email", "/api/v1/members/duplicate/nickname",
            "/h2-console", "/swagger-ui", "/api-docs"};

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * filter 동작 제외
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(EXCLUDED_URL).anyMatch(request.getRequestURI()::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // header에서 access token 추출
        log.info("resolve access token from header");
        String token = jwtTokenProvider.resolveToken(request);
        // access token 유효성 검증
        if (!Objects.isNull(token) && jwtTokenProvider.validateAccessToken(token)) {
            log.info("access token is valid");

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
