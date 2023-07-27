package com.example.honjarang.domain.user.service;

import com.example.honjarang.domain.user.dto.LoginDto;
import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.entity.EmailVerification;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.DuplicateNicknameException;
import com.example.honjarang.domain.user.exception.EmailNotVerifiedException;
import com.example.honjarang.domain.user.exception.PasswordMismatchException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.EmailVerificationRepository;
import com.example.honjarang.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private User user;
    private EmailVerification emailVerification;

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
        emailVerification = EmailVerification.builder()
                .email("test@test.com")
                .code("123456")
                .isVerified(true)
                .build();
        emailVerification.setIdForTest(1L);
    }


    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");

        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("test1234", "test1234")).willReturn(true);

        // when
        User user = userService.login(loginDto);

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

    @Test
    @DisplayName("로그인 실패 - 사용자가 존재하지 않는 경우")
    public void login_UserNotFoundException() {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.login(loginDto));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 일치하지 않는 경우")
    public void login_PasswordMismatchException() {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("test1234", "test1234")).willReturn(false);

        // when & then
        assertThrows(PasswordMismatchException.class, () -> userService.login(loginDto));
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // given
        UserCreateDto userCreateDto = new UserCreateDto("test@test.com", "test1234", "테스트", "서울특별시 강남구", 37.123456, 127.123456);

        given(emailVerificationRepository.findByEmail("test@test.com")).willReturn(Optional.of(emailVerification));

        // when
        userService.signup(userCreateDto);

        // then
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 인증이 되지 않은 경우")
    void signup_EmailNotVerifiedException() {
        // given
        emailVerification.setIsVerifiedForTest(false);
        UserCreateDto userCreateDto = new UserCreateDto("test@test.com", "test1234", "테스트", "서울특별시 강남구", 37.123456, 127.123456);

        given(emailVerificationRepository.findByEmail("test@test.com")).willReturn(Optional.of(emailVerification));

        // when & then
        assertThrows(EmailNotVerifiedException.class, () -> userService.signup(userCreateDto));
    }

    @Test
    @DisplayName("닉네임 중복 체크 성공")
    void checkNickname_Success() {
        // given
        given(userRepository.existsByNickname("테스트")).willReturn(false);

        // when
        userService.checkNickname("테스트");

        // then
    }

    @Test
    @DisplayName("닉네임 중복 체크 실패 - 중복된 닉네임인 경우")
    void checkNickname_DuplicateNicknameException() {
        // given
        given(userRepository.existsByNickname("테스트")).willReturn(true);

        // when & then
        assertThrows(DuplicateNicknameException.class, () -> userService.checkNickname("테스트"));
    }


    @Test
    @DisplayName("비밀번호 변경 성공")
    public void changePassword_Success() {
        // given
        given(passwordEncoder.matches("test1234", "test1234")).willReturn(true);

        // when
        userService.changePassword(user, "test1234", "test1234");

        // then
        assertThat(user.getPassword()).isEqualTo("test1234");
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 사용자가 입력한 현재 비밀번호가 일치하지 않는 경우")
    public void changePassword_PasswordMismatchException() {
        // given
        given(passwordEncoder.matches("test1234", "test1234")).willReturn(false);

        // When
        assertThrows(PasswordMismatchException.class, () -> userService.changePassword(user, "test1234", "test1234"));
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void changeUserInfo_Success() {
        // given

        // when
        userService.changeUserInfo(user, "테스트", "서울특별시 강남구");

        // then
        assertThat(user.getNickname()).isEqualTo("테스트");
        assertThat(user.getAddress()).isEqualTo("서울특별시 강남구");
    }


}