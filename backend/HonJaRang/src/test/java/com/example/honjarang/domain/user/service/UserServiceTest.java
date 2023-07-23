package com.example.honjarang.domain.user.service;

import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.entity.EmailVerification;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.DuplicateNicknameException;
import com.example.honjarang.domain.user.exception.EmailNotVerifiedException;
import com.example.honjarang.domain.user.exception.PasswordMismatchException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.EmailVerificationRepository;
import com.example.honjarang.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "test1234";
    private static final String TEST_NICKNAME = "test";
    private static final String TEST_ADDRESS = "서울특별시 강남구";
    private static final Double TEST_LATITUDE = 37.123456;
    private static final Double TEST_LONGITUDE = 127.123456;
    private static final String TEST_CODE = "test";

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        User expectedUser = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(expectedUser));
        given(passwordEncoder.matches(TEST_PASSWORD, expectedUser.getPassword())).willReturn(true);

        // when
        User user = userService.login(TEST_EMAIL, TEST_PASSWORD);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(user.getPassword()).isEqualTo(TEST_PASSWORD);
    }

    @Test
    @DisplayName("로그인 실패 - 사용자가 존재하지 않는 경우")
    public void login_UserNotFoundException() {
        // given
        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.login(TEST_EMAIL, TEST_PASSWORD));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 일치하지 않는 경우")
    public void login_PasswordMismatchException() {
        // given
        User expectedUser = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(expectedUser));
        given(passwordEncoder.matches(TEST_PASSWORD, expectedUser.getPassword())).willReturn(false);

        // when & then
        assertThrows(PasswordMismatchException.class, () -> userService.login(TEST_EMAIL, TEST_PASSWORD));
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // given
        UserCreateDto userCreateDto = new UserCreateDto(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME, TEST_ADDRESS, TEST_LATITUDE, TEST_LONGITUDE);
        EmailVerification emailVerification = EmailVerification.builder()
                .email(TEST_EMAIL)
                .code(TEST_CODE)
                .isVerified(true)
                .build();

        given(emailVerificationRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(emailVerification));

        // when
        userService.signup(userCreateDto);

        // then

    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 인증이 되지 않은 경우")
    void signup_EmailNotVerifiedException() {
        // given
        UserCreateDto userCreateDto = new UserCreateDto(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME, TEST_ADDRESS, TEST_LATITUDE, TEST_LONGITUDE);
        EmailVerification emailVerification = EmailVerification.builder()
                .email(TEST_EMAIL)
                .code(TEST_CODE)
                .isVerified(false)
                .build();

        given(emailVerificationRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(emailVerification));

        // when & then
        assertThrows(EmailNotVerifiedException.class, () -> userService.signup(userCreateDto));
    }

    @Test
    @DisplayName("닉네임 중복 체크 성공")
    void checkNickname_Success() {
        // given
        given(userRepository.existsByNickname("test")).willReturn(false);

        // when
        Boolean isAvailable = userService.checkNickname("test");

        // then
        assertThat(isAvailable).isTrue();
    }

    @Test
    @DisplayName("닉네임 중복 체크 실패 - 중복된 닉네임인 경우")
    void checkNickname_DuplicateNicknameException() {
        // given
        given(userRepository.existsByNickname("test")).willReturn(true);

        // when & then
        assertThrows(DuplicateNicknameException.class, () -> userService.checkNickname("test"));
    }
}