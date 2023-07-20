package com.example.honjarang.security.service;

import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.example.honjarang.security.dto.TokenDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenService {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private static final long ACCESS_TOKEN_EXPIRE_SECONDS = 60 * 60; // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24 * 30; // 30일

    private final UserRepository userRepository;

    public TokenDto generateToken(String email, Role role) {
        // access token 생성
        String accessToken = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // refresh token 생성
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    public boolean verifyToken(String token) {
        // token이 유효기간이 만료되었는지 확인
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody().getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public User getUserByToken(String token) {
        String email = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
