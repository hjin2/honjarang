package com.example.honjarang.security.service;

import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.exception.InvalidTokenException;
import com.example.honjarang.security.exception.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final String SECRET_KEY = "honjarang";
    private static final long ACCESS_TOKEN_EXPIRE_SECONDS = 60 * 60; // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24 * 30; // 30일

    private final UserRepository userRepository;

    public TokenDto generateToken(Long id, String email, Role role) {
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

        return new TokenDto(id, accessToken, refreshToken);
    }

    public Boolean verifyToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("만료된 토큰입니다.");
        } catch (Exception e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }

    @Transactional(readOnly = true)
    public User getUserByToken(String token) {
        String email = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
