package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.PasswordMismatchException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "test1234";

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
    @DisplayName("비밀번호 변경 성공")
    void changePassword_Success() throws Exception {
        // given
        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();


        // when & then
        mockMvc.perform(put("/api/v1/users/change-password")
                        .param("existPassword", TEST_PASSWORD)
                        .param("newPassword", "newtest1234"))
                .andExpect(status().isOk());

    }

}