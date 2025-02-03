package com.example.team5_project.common.utils;

import com.example.team5_project.common.enums.UserType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    @Value("asjdkjfxcjvxbjfsdrjhweuhrkalsdjjczxjhkjfhsadsjklas")
    private String secretKey;
    private Key key;

    // 초기 설정
    @PostConstruct
    public void init() {

        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 정보 추출
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 생성
    public String generateToken(Long id, String name, UserType user_type) {

        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(id))
                        .claim("name", name)
                        .claim("user_type", user_type)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String extractId(String token) {

        return extractAllClaims(token).getSubject();
    }

    public String extractUserType(String token) {

        return extractAllClaims(token).get("user_type", String.class);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)  // 비밀 키 설정
                    .build()  // parser 빌더를 통한 빌드
                    .parseClaimsJws(token);  // 토큰 파싱 및 검증
            return true;  // 토큰이 유효한 경우
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.", e);
        }

        return false;  // 예외가 발생해서 토큰이 유효하지 않은 경우
    }
}
