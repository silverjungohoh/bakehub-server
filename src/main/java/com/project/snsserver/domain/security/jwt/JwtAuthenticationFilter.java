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
 * token 유효한지 검증
 * 검증 성공 시 Security Context에 Authentication 객체 저장
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] EXCLUDED_URL
            = {"/api/v1/members/sign-up", "/api/v1/members/auth/login",
            "/api/v1/members/duplicate/email", "/api/v1/members/duplicate/nickname",
            "/api/v1/members/send/email", "/api/v1/members/verify/email",
            "/h2-console", "/swagger-ui", "/api-docs"};
    private static final String REISSUE_TOKEN_URL = "/api/v1/members/auth/token";

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

        String token;

        // token 재발급 요청인 경우
        if(request.getRequestURI().startsWith(REISSUE_TOKEN_URL)) {
            log.info("resolve refresh token from header");
            // header에서 refresh token 추출
            token = request.getHeader("RTK");
        } else {
            // header에서 access token 추출
            log.info("resolve access token from header");
            token = jwtTokenProvider.resolveAccessToken(request);
        }

        // token 유효성 검증
        if (!Objects.isNull(token) && jwtTokenProvider.validateToken(token)) {
            log.info("token is valid");

            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            log.info("save authentication object in SecurityContext");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
