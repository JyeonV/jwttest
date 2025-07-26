package com.example.jwttest.common.jwt;

import com.example.jwttest.common.code.ErrorStatus;
import com.example.jwttest.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60; // 어세스 토큰 만료 시간
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 리프레시 토큰 만료 시간

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes()); // secretkey를 단순한 문자열에서 jwt 서명에 쓸수있는 key 객체로 바꿔주는 역할
    }

    public String createAccessToken(Long userId, String username, UserRole userRole) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 주제설정, string 으로만 주입 가능
                .claim("username", username)
                .claim("userRole", userRole)
                .setIssuedAt(now) // 발급날짜
                .setExpiration(expiration) // 만료날짜
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public String createRefreshToken(Long userId) { // refresh 토큰 생성 메서드
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 주제설정, string 으로만 주입 가능
                .setIssuedAt(now) // 발급날짜
                .setExpiration(expiration) // 만료날짜
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public Claims getClaims(String token) { // jwt 토큰을 파싱하고 내부(payload)에 있는 정보들을 꺼내오기 위함
        return Jwts.parserBuilder() // 토큰을 검증하고 해석하려면 parserBuilder로 시작해야함
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) { // 토큰 유효성 검증
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public String resolveAccessToken(HttpServletRequest request) { // 토큰 추출 from Header 메서드
        String bearerJwt = request.getHeader("Authorization");
        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            throw new JwtCustomException(ErrorStatus.INVALID_TOKEN);
        }
        String token = bearerJwt.substring(7);
        return token;
    }

    public Long getExpiration(String token) { // 남은 유효시간 확인 메서드
        Claims claims = getClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis(); // 남은시간을 ms 단위로 계산해서 반환
    }

    public boolean isExpired(String token) { // 만료 여부 확인 메서드
        Claims claims = getClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}
