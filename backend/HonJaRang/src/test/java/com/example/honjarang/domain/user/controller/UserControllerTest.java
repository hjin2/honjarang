package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.dto.*;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.service.EmailService;
import com.example.honjarang.domain.user.service.S3UploadService;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
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
    private S3UploadService s3UploadService;

    private User user;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("honjarang.kro.kr")
                        .withPort(80))
                .build();
        user = User.builder()
                .email("test@test.com")
                .password("test1234")
                .nickname("테스트")
                .point(10000)
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
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
                .andExpect(jsonPath("$.refresh_token").value("refresh_token"))
                .andDo(document("users/login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("access_token").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("리프레시 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("이메일 인증번호 전송")
    void sendVerificationCode() throws Exception {
        // given
        Map<String, String> body = Map.of("email", "test@test.com");

        // when & then
        mockMvc.perform(post("/api/v1/users/send-verification-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("users/send-verification-code",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        )
                ));
    }

    @Test
    @DisplayName("이메일 인증번호 검증")
    void verifyCode() throws Exception {
        // given
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto("test@test.com", "123456");

        // when & then
        mockMvc.perform(post("/api/v1/users/verify-code")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(verifyCodeDto)))
                .andExpect(status().isOk())
                .andDo(document("users/verify-code",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("인증번호")
                        )
                ));
    }

    @Test
    @DisplayName("닉네임 중복 검사")
    void checkNickname() throws Exception {
        // given

        // when & then
        mockMvc.perform(get("/api/v1/users/check-nickname")
                        .param("nickname", "닉네임"))
                .andExpect(status().isOk())
                .andDo(document("users/check-nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("nickname").description("닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("회원가입")
    void signup() throws Exception {
        // given
        UserCreateDto userCreateDto = new UserCreateDto("test@test.com", "test1234", "테스트", "서울특별시 강남구", 37.123456, 127.123456);

        // when & then
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userCreateDto)))
                .andExpect(status().isOk())
                .andDo(document("users/signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")
                        )
                ));
    }

    @Test
    @DisplayName("회원정보 수정")
    void changeUserInfo() throws Exception {
        // given
        UserInfoUpdateDto userInfoUpdateDto = new UserInfoUpdateDto("테스트", "서울특별시 강남구", 50.1234, 60.1234);

        // when & then
        mockMvc.perform(put("/api/v1/users/users")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userInfoUpdateDto)))
                .andExpect(status().isOk())
                .andDo(document("users/change-user-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePassword() throws Exception {
        // given
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto("test1234", "test1234");

        // when & then
        mockMvc.perform(put("/api/v1/users/change-password")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(passwordUpdateDto)))
                .andExpect(status().isOk())
                .andDo(document("users/change-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("new_password").type(JsonFieldType.STRING).description("새 비밀번호")
                        )
                ));
    }
}