package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.*;
import com.example.honjarang.domain.user.service.EmailService;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "test1234";
    private static final String TEST_CODE = "test";

    @Test
    @DisplayName("로그인 성공")
    void login_Success() throws Exception {
        // given
        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        TokenDto expectedTokenDto = new TokenDto("accessToken", "refreshToken");

        given(userService.login(TEST_EMAIL, TEST_PASSWORD)).willReturn(user);
        given(tokenService.generateToken(TEST_EMAIL, user.getRole())).willReturn(expectedTokenDto);

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .param("email", TEST_EMAIL)
                        .param("password", TEST_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(expectedTokenDto.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(expectedTokenDto.getRefreshToken()));
    }

    @Test
    @DisplayName("로그인 실패 - 사용자가 존재하지 않는 경우")
    public void login_UserNotFoundException() throws Exception {
        // given
        given(userService.login(TEST_EMAIL, TEST_PASSWORD)).willThrow(new UserNotFoundException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .param("email", TEST_EMAIL)
                        .param("password", TEST_PASSWORD))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 틀린 경우")
    public void login_PasswordMismatchException() throws Exception {
        // given
        given(userService.login(TEST_EMAIL, TEST_PASSWORD)).willThrow(new PasswordMismatchException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .param("email", TEST_EMAIL)
                        .param("password", TEST_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 인증번호 전송")
    void sendVerificationCode() throws Exception {
        // given

        // when & then
        mockMvc.perform(post("/api/v1/users/send-verification-code")
                        .param("email", TEST_EMAIL))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 인증번호 검증 성공")
    void verifyCode_Success() throws Exception{
        // given
        given(emailService.verifyCode(TEST_EMAIL, "test")).willReturn(true);

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .param("email", TEST_EMAIL)
                        .param("code", TEST_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호가 틀린 경우")
    void verifyCode_VerificationCodeMismatchException() throws Exception{
        // given
        given(emailService.verifyCode(TEST_EMAIL, TEST_CODE)).willThrow(new VerificationCodeMismatchException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .param("email", TEST_EMAIL)
                        .param("code", TEST_CODE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호가 만료된 경우")
    void verifyCode_VerificationCodeExpiredException() throws Exception{
        // given
        given(emailService.verifyCode(TEST_EMAIL, TEST_CODE)).willThrow(new VerificationCodeExpiredException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .param("email", TEST_EMAIL)
                        .param("code", TEST_CODE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 이미 인증된 이메일인 경우")
    void verifyCode_EmailAlreadyVerifiedException() throws Exception{
        // given
        given(emailService.verifyCode(TEST_EMAIL, TEST_CODE)).willThrow(new EmailAlreadyVerifiedException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .param("email", TEST_EMAIL)
                        .param("code", TEST_CODE))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호가 존재하지 않는 경우")
    void verifyCode_VerificationCodeNotFoundException() throws Exception{
        // given
        given(emailService.verifyCode(TEST_EMAIL, TEST_CODE)).willThrow(new VerificationCodeNotFoundException(""));

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .param("email", TEST_EMAIL)
                        .param("code", TEST_CODE))
                .andExpect(status().isNotFound());
    }
}