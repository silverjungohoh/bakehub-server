package com.project.snsserver.domain.security.jwt;

import com.project.snsserver.domain.member.model.entity.RefreshToken;
import com.project.snsserver.domain.member.repository.redis.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.valid.accessToken}")
    private Long accessTokenValid;

    @Value("${spring.jwt.valid.refreshToken}")
    private Long refreshTokenValid;

    private final RefreshTokenRepository refreshTokenRepository;


    private Key getKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
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
                .expiredAt(refreshTokenValid)
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}
