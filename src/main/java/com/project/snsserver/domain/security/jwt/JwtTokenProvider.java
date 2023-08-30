package com.project.snsserver.domain.security.jwt;

import com.project.snsserver.domain.member.model.entity.RefreshToken;
import com.project.snsserver.domain.member.repository.redis.LogoutAccessTokenRepository;
import com.project.snsserver.domain.member.repository.redis.RefreshTokenRepository;
import com.project.snsserver.domain.security.service.CustomUserDetailsService;
import com.project.snsserver.global.error.exception.CustomJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.project.snsserver.global.error.type.JwtErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.valid.accessToken}")
    private Long accessTokenValid;

    @Value("${spring.jwt.valid.refreshToken}")
    private Long refreshTokenValid;

    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService userDetailsService;
    private final LogoutAccessTokenRepository logoutAccessTokenRepository;


    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(Map<String, Object> claims, long expireTime) {

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        Date now = new Date();
        return Jwts.builder()
                .setHeader(header)
                .setSubject("AUTH")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * access token 발급
     */
    public String generateAccessToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", role);

        return generateToken(claims, accessTokenValid);
    }

    /**
     * refresh token 발급
     */
    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        String refreshToken = generateToken(claims, refreshTokenValid);
        saveRefreshToken(email, refreshToken);
        return refreshToken;
    }

    /**
     * refresh token redis 저장
     */
    public void saveRefreshToken(String email, String token) {

        RefreshToken refreshToken = RefreshToken.builder()
                .id(email)
                .refreshToken(token)
                .expiration(refreshTokenValid)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * request header에서 access token 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
            return headerAuth.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * access token 유효성 검증
     */
    public boolean validateAccessToken(String token) {
        return !extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * refresh token 유효성 검증
     */
    public boolean validateRefreshToken(String token) {
        try {
            return !extractClaims(token).getExpiration().before(new Date());
        } catch (CustomJwtException e) {
            return false;
        }
    }

    /**
     * token 유효 시간 계산
     */
    public Long getRemainingTime(String token) {
        Date expiredAt = extractClaims(token).getExpiration();
        Date now = new Date();
        return now.getTime() - expiredAt.getTime();
    }

    public Claims extractClaims(String token) {
        try {
            if(logoutAccessTokenRepository.existsById(token)) {
                log.error("already logout");
                throw new CustomJwtException(ALREADY_LOGOUT_TOKEN);
            }
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            throw new CustomJwtException(EXPIRED_TOKEN);
        } catch (SecurityException | MalformedJwtException e) {
            log.error(e.getMessage());
            throw new CustomJwtException(INVALID_TOKEN_SIGNATURE);
        } catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
            throw new CustomJwtException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new CustomJwtException(INCORRECT_TOKEN);
        }
    }

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.get("email", String.class);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails
                = userDetailsService.loadUserByUsername(extractUsername(token));

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
