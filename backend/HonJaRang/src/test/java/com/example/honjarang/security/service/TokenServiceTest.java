package com.example.honjarang.security.service;

import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Test
    @DisplayName("토큰 생성 테스트")
    void generateToken() {
        // given
        String email = "test@test.com";
        Role role = Role.ROLE_USER;

        // when
        TokenDto tokenDto = tokenService.generateToken(email, role);

        // then
        assertThat(tokenDto).isNotNull();
    }

    @Test
    @DisplayName("토큰 검증 테스트")
    void verifyToken() {
        // 토큰의 유효기간이 만료되지 않은 경우
        {
            // given
            String email = "test@test.com";
            Role role = Role.ROLE_USER;

            String token = Jwts.builder()
                    .setSubject(email)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();

            // when
            Boolean result = tokenService.verifyToken(token);

            // then
            assertThat(result).isTrue();
        }
        // 토큰의 유효기간이 만료된 경우
        {
            // given
            String email = "test@test.com";
            Role role = Role.ROLE_USER;

            String token = Jwts.builder()
                    .setSubject(email)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() - 60 * 60 * 1000))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();

            // when
            Boolean result = tokenService.verifyToken(token);

            // then
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("토큰에서 사용자 정보 추출 테스트")
    void getUserByToken() {
        // given
        String email = "test@test.com";
        Role role = Role.ROLE_USER;

        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // when
        User user = tokenService.getUserByToken(token);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getRole()).isEqualTo(role);
    }
}