package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.dto.*;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.*;
import com.example.honjarang.domain.user.service.EmailService;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private EmailService emailService;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .email("test@test.com")
                .password("test1234")
                .nickname("테스트")
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() throws Exception {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        TokenDto tokenDto = new TokenDto("access_token", "refresh_token");

        given(userService.login(any(LoginDto.class))).willReturn(user);
        given(tokenService.generateToken("test@test.com", Role.ROLE_USER)).willReturn(tokenDto);

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access_token"))
                .andExpect(jsonPath("$.refresh_token").value("refresh_token"));
    }

    @Test
    @DisplayName("로그인 실패 - 사용자가 존재하지 않는 경우")
    public void login_UserNotFoundException() throws Exception {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        given(userService.login(any(LoginDto.class))).willThrow(new UserNotFoundException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 틀린 경우")
    public void login_PasswordMismatchException() throws Exception {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        given(userService.login(any(LoginDto.class))).willThrow(new PasswordMismatchException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 인증번호 전송 성공")
    void sendVerificationCode_Success() throws Exception {
        // given
        Map<String, String> body = Map.of("email", "test@test.com");

        // when & then
        mockMvc.perform(post("/api/v1/users/send-verification-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 인증번호 전송 실패 - 이미 가입된 이메일인 경우")
    void sendVerificationCode_DuplicateEmailException() throws Exception {
        // given
        Map<String, String> body = Map.of("email", "test@test.com");
        doThrow(new DuplicateEmailException("")).when(emailService).sendVerificationCode("test@test.com");

        // when & then
        mockMvc.perform(post("/api/v1/users/send-verification-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("이메일 인증번호 검증 성공")
    void verifyCode_Success() throws Exception {
        // given
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto("test@test.com", "123456");
        given(emailService.verifyCode("test@test.com", "123456")).willReturn(true);

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(verifyCodeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호가 틀린 경우")
    void verifyCode_VerificationCodeMismatchException() throws Exception {
        // given
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto("test@test.com", "123456");
        given(emailService.verifyCode("test@test.com", "123456")).willThrow(new VerificationCodeMismatchException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(verifyCodeDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호가 만료된 경우")
    void verifyCode_VerificationCodeExpiredException() throws Exception {
        // given
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto("test@test.com", "123456");
        given(emailService.verifyCode("test@test.com", "123456")).willThrow(new VerificationCodeExpiredException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(verifyCodeDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 이미 인증된 이메일인 경우")
    void verifyCode_EmailAlreadyVerifiedException() throws Exception {
        // given
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto("test@test.com", "123456");
        given(emailService.verifyCode("test@test.com", "123456")).willThrow(new EmailAlreadyVerifiedException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(verifyCodeDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호가 존재하지 않는 경우")
    void verifyCode_VerificationCodeNotFoundException() throws Exception {
        // given
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto("test@test.com", "123456");
        given(emailService.verifyCode("test@test.com", "123456")).willThrow(new VerificationCodeNotFoundException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(verifyCodeDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("닉네임 중복 검사 성공")
    void checkNickname_Success() throws Exception {
        // given
        given(userService.checkNickname("닉네임")).willReturn(true);

        // when & then
        mockMvc.perform(get("/api/v1/users/check-nickname")
                        .param("nickname", "닉네임"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("닉네임 중복 검사 실패 - 중복된 닉네임인 경우")
    void checkNickname_DuplicateNicknameException() throws Exception {
        // given
        given(userService.checkNickname("닉네임")).willThrow(new DuplicateNicknameException(""));

        // when & then
        mockMvc.perform(get("/api/v1/users/check-nickname")
                        .param("nickname", "닉네임"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() throws Exception {
        // given
        UserCreateDto userCreateDto = new UserCreateDto("test@test.com", "test1234", "테스트", "서울특별시 강남구", 37.123456, 127.123456);

        // when & then
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userCreateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 인증이 되지 않은 경우")
    void signup_EmailNotVerifiedException() throws Exception {
        // given
        UserCreateDto userCreateDto = new UserCreateDto("test@test.com", "test1234", "테스트", "서울특별시 강남구", 37.123456, 127.123456);
        doThrow(new EmailNotVerifiedException("")).when(userService).signup(any(UserCreateDto.class));

        // when & then
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userCreateDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void changeUserInfo_Success() throws Exception {
        // given
        UserInfoUpdateDto userInfoUpdateDto = new UserInfoUpdateDto("테스트", "서울특별시 강남구");

        // when & then
        mockMvc.perform(put("/api/v1/users/users")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userInfoUpdateDto)))
                .andExpect(status().isOk());
}




    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword_Success() throws Exception {
        // given
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto("test1234", "test1234");

        // when & then
        mockMvc.perform(put("/api/v1/users/change-password")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(passwordUpdateDto)))
                .andExpect(status().isOk());
    }
}