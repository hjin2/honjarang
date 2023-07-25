package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.dto.LoginDto;
import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.dto.VerifyCodeDto;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.*;
import com.example.honjarang.domain.user.service.EmailService;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockBean
    private MongoTemplate mongoTemplate;

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "test1234";
    private static final String TEST_CODE = "test";
    private static final String TEST_NICKNAME = "test";
    private static final String TEST_ADDRESS = "서울특별시 강남구";
    private static final Double TEST_LATITUDE = 37.123456;
    private static final Double TEST_LONGITUDE = 127.123456;

    @Test
    @DisplayName("로그인 성공")
    void login_Success() throws Exception {
        // given
        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
        LoginDto loginDto = new LoginDto(TEST_EMAIL, TEST_PASSWORD);
        TokenDto expectedTokenDto = new TokenDto("accessToken", "refreshToken");

        given(userService.login(TEST_EMAIL, TEST_PASSWORD)).willReturn(user);
        given(tokenService.generateToken(TEST_EMAIL, user.getRole())).willReturn(expectedTokenDto);

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(expectedTokenDto.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(expectedTokenDto.getRefreshToken()));
    }

    @Test
    @DisplayName("로그인 실패 - 사용자가 존재하지 않는 경우")
    public void login_UserNotFoundException() throws Exception {
        // given
        LoginDto loginDto = new LoginDto(TEST_EMAIL, TEST_PASSWORD);
        given(userService.login(TEST_EMAIL, TEST_PASSWORD)).willThrow(new UserNotFoundException(""));

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
        LoginDto loginDto = new LoginDto(TEST_EMAIL, TEST_PASSWORD);
        given(userService.login(TEST_EMAIL, TEST_PASSWORD)).willThrow(new PasswordMismatchException(""));

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
        Map<String, String> body = Map.of("email", TEST_EMAIL);

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
        Map<String, String> body = Map.of("email", TEST_EMAIL);
        doThrow(new DuplicateEmailException("")).when(emailService).sendVerificationCode(TEST_EMAIL);

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
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto(TEST_EMAIL, TEST_CODE);
        given(emailService.verifyCode(TEST_EMAIL, "test")).willReturn(true);

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
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto(TEST_EMAIL, TEST_CODE);
        given(emailService.verifyCode(TEST_EMAIL, TEST_CODE)).willThrow(new VerificationCodeMismatchException(""));

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
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto(TEST_EMAIL, TEST_CODE);
        given(emailService.verifyCode(TEST_EMAIL, TEST_CODE)).willThrow(new VerificationCodeExpiredException(""));

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
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto(TEST_EMAIL, TEST_CODE);
        given(emailService.verifyCode(TEST_EMAIL, TEST_CODE)).willThrow(new EmailAlreadyVerifiedException(""));

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
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto(TEST_EMAIL, TEST_CODE);
        given(emailService.verifyCode(TEST_EMAIL, TEST_CODE)).willThrow(new VerificationCodeNotFoundException(""));

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
        given(userService.checkNickname(TEST_NICKNAME)).willReturn(true);

        // when & then
        mockMvc.perform(get("/api/v1/users/check-nickname")
                        .param("nickname", TEST_NICKNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("닉네임 중복 검사 실패 - 중복된 닉네임인 경우")
    void checkNickname_DuplicateNicknameException() throws Exception {
        // given
        given(userService.checkNickname(TEST_NICKNAME)).willThrow(new DuplicateNicknameException(""));

        // when & then
        mockMvc.perform(get("/api/v1/users/check-nickname")
                        .param("nickname", TEST_NICKNAME))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() throws Exception {
        // given
        UserCreateDto userCreateDto = new UserCreateDto(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME, TEST_ADDRESS, TEST_LATITUDE, TEST_LONGITUDE);

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
        UserCreateDto userCreateDto = new UserCreateDto(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME, TEST_ADDRESS, TEST_LATITUDE, TEST_LONGITUDE);
        doThrow(new EmailNotVerifiedException("")).when(userService).signup(any(UserCreateDto.class));

        // when & then
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userCreateDto)))
                .andExpect(status().isForbidden());
    }
}