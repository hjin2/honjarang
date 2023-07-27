package com.example.honjarang.security.service;

import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.exception.InvalidTokenException;
import com.example.honjarang.security.exception.TokenExpiredException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    private User user;

    private final String TEST_SECRET_KEY = "honjarang";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@test.com")
                .password("test1234")
                .nickname("테스트")
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .role(Role.ROLE_USER)
                .build();
        user.setIdForTest(1L);
    }

    @Test
    @DisplayName("토큰 생성")
    void generateToken() {
        // given

        // when
        TokenDto tokenDto = tokenService.generateToken("test@test.com", Role.ROLE_USER);

        // then
        assertThat(tokenDto).isNotNull();
        assertThat(tokenDto.getAccessToken()).isNotNull();
        assertThat(tokenDto.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("토큰 검증 성공")
    void verifyToken_Success() {
        // given
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .claim("role", Role.ROLE_USER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, TEST_SECRET_KEY)
                .compact();

        // when
        Boolean result = tokenService.verifyToken(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰 검증 실패 - 토큰이 만료된 경우")
    void verifyToken_TokenExpiredException() {
        // given
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .claim("role", Role.ROLE_USER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, TEST_SECRET_KEY)
                .compact();

        // when & then
        assertThrows(TokenExpiredException.class, () -> tokenService.verifyToken(token));
    }

    @Test
    @DisplayName("토큰 검증 실패 - 토큰이 유효하지 않은 경우")
    void verifyToken_InvalidTokenException() {
        // given
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .claim("role", Role.ROLE_USER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, "invalidSecretKey")
                .compact();

        // when & then
        assertThrows(InvalidTokenException.class, () -> tokenService.verifyToken(token));
    }

    @Test
    @DisplayName("토큰에서 사용자 정보 추출 테스트")
    void getUserByToken() {
        // given
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .claim("role", Role.ROLE_USER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, TEST_SECRET_KEY)
                .compact();

        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));

        // when
        User user = tokenService.getUserByToken(token);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getPassword()).isEqualTo("test1234");
        assertThat(user.getNickname()).isEqualTo("테스트");
        assertThat(user.getAddress()).isEqualTo("서울특별시 강남구");
        assertThat(user.getLatitude()).isEqualTo(37.123456);
        assertThat(user.getLongitude()).isEqualTo(127.123456);
        assertThat(user.getRole()).isEqualTo(Role.ROLE_USER);
    }
}