package com.project.snsserver.domain.security.jwt;

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

        return generateToken(claims, refreshTokenValid);
    }
}
