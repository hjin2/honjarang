package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointdelivery.dto.JointDeliveryListDto;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.user.dto.*;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.service.EmailService;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private User user;
    private Post post;
    private PostListDto postListDto;
    private JointDeliveryListDto jointDeliveryListDto;
    private JointDelivery jointDelivery;
    private Store store;

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
        user.changeProfileImage("test.jpg");
        user.setIdForTest(1L);
        post = Post.builder()
                .user(user)
                .views(1)
                .title("타이틀")
                .category(Category.FREE)
                .isNotice(false)
                .content("내용")
                .build();
        post.setIdForTest(1L);
        post.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2023-08-02 12:00:00"));
        store = Store.builder()
                .id(1L)
                .storeName("가게명")
                .image("storeImage.jpg")
                .address("경상북도 구미시")
                .latitude(23.4567)
                .longitude(34.5678).build();
        jointDelivery = JointDelivery.builder()
                .targetMinPrice(20000)
                .deliveryCharge(3000)
                .content("치킨")
                .deadline(DateTimeUtils.parseLocalDateTime("2023-08-02 12:23:34"))
                .store(store)
                .user(user)
                .build();
        jointDelivery.setIdForTest(1L);


        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        TokenDto tokenDto = new TokenDto(1L, "access_token", "refresh_token");

        given(userService.login(any(LoginDto.class))).willReturn(user);
        given(tokenService.generateToken(1L, "test@test.com", Role.ROLE_USER)).willReturn(tokenDto);

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1L))
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
                                fieldWithPath("user_id").type(JsonFieldType.NUMBER).description("유저 아이디"),
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
    @DisplayName("이메일 중복 검사")
    void checkEmail() throws Exception{
        // given

        // when & then
        mockMvc.perform(get("/api/v1/users/check-email")
                        .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andDo(document("users/check-email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("email").description("이메일")
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
    @DisplayName("포인트 충전")
    void successPayment_success() throws Exception {
        PointChargeDto pointChargeDto = new PointChargeDto("temppaymentkey","temporderid",10000);

        mockMvc.perform(post("/api/v1/users/success")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(pointChargeDto)))
                .andExpect(status().isOk())
                .andDo(document("users/payment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("payment_key").type(JsonFieldType.STRING).description("페이먼트 키"),
                                fieldWithPath("order_id").type(JsonFieldType.STRING).description("주문 ID"),
                                fieldWithPath("amount").type(JsonFieldType.NUMBER).description("금액")
                        )
                ));
    }



    @Test
    @DisplayName("포인트 환급")
    void getJointDelivery() throws Exception {
        // given
        PointWithdrawDto pointWithdrawDto = new PointWithdrawDto(10000,"테스트사용자","75220121039","sc제일은행");

        // when & then
        mockMvc.perform(put("/api/v1/users/withdraw")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(pointWithdrawDto)))
                .andExpect(status().isOk())
                .andDo(document("users/withdraw",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("point").type(JsonFieldType.NUMBER).description("포인트"),
                                fieldWithPath("account_holder").type(JsonFieldType.STRING).description("예금주"),
                                fieldWithPath("account_number").type(JsonFieldType.STRING).description("계좌번호"),
                                fieldWithPath("bank").type(JsonFieldType.STRING).description("은행")
                        )
                ));
        }

//    @Test
//    @DisplayName("회원정보 조회")
//    void getUserInfo_success() throws Exception {
//        // given
//        UserInfoDto userInfoDto = new UserInfoDto(user);
//        given(userService.getUserInfo(1L)).willReturn(userInfoDto);
//
//        // when & then
//        mockMvc.perform(get("/api/v1/users/info")
//                        .param("id", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.nickname").value("테스트"))
//                .andExpect(jsonPath("$.email").value("test@test.com"))
//                .andExpect(jsonPath("$.profile_image").value("https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/test.jpg"))
//                .andExpect(jsonPath("$.point").value(10000))
//                .andExpect(jsonPath("$.address").value("서울특별시 강남구"))
//                .andExpect(jsonPath("$.latitude").value(37.123456))
//                .andExpect(jsonPath("$.longitude").value(127.123456))
//                .andDo(document("/users/info",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        queryParameters(
//                                parameterWithName("id").description("사용자 ID")
//                        ),
//                        responseFields(
//                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
//                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
//                                fieldWithPath("profile_image").type(JsonFieldType.STRING).description("프로필 이미지"),
//                                fieldWithPath("point").type(JsonFieldType.NUMBER).description("포인트"),
//                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
//                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
//                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")
//                        )
//                ));
//    }

    @Test
    @DisplayName("FCM 토큰 등록")
    void updateFcmToken() throws Exception{
        // given
        Map<String, String> body = Map.of("fcm_token", "fcm_token");

        // when & then
        mockMvc.perform(post("/api/v1/users/fcm-token")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("users/fcm-token",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("fcm_token").type(JsonFieldType.STRING).description("FCM 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUser_suceess() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/v1/users/{userId}",1L))
                .andExpect(status().isOk())
                .andDo(document("/users/deleteUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("사용자 ID")
                        ))
                );
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
        // given
        Map<String, String> body = Map.of("fcm_token", "fcm_token");

        // when & then
        mockMvc.perform(post("/api/v1/users/logout")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("users/logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("fcm_token").type(JsonFieldType.STRING).description("FCM 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("프로필 이미지 업로드")
    void uploadProfileImage() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("profile_image", "test.jpg", "image/jpeg", "test".getBytes());

        // when & then
        mockMvc.perform(multipart("/api/v1/users/profile-image")
                        .file(file))
                .andExpect(status().isOk())
                .andDo(document("users/profile-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("profile_image").description("프로필 이미지")
                        )
                ));
    }

    @Test
    @DisplayName("토큰 갱신")
    void refresh() throws Exception{
        // given
        Map<String, String> body = Map.of("refresh_token", "refresh_token");
        TokenDto tokenDto = new TokenDto(1L, "access_token", "refresh_token");

        given(tokenService.generateToken(1L, "test@test.com", Role.ROLE_USER)).willReturn(tokenDto);

        // when & then
        mockMvc.perform(post("/api/v1/users/refresh")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1L))
                .andExpect(jsonPath("$.access_token").value("access_token"))
                .andExpect(jsonPath("$.refresh_token").value("refresh_token"))
                .andDo(document("users/refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("user_id").type(JsonFieldType.NUMBER).description("유저 아이디"),
                                fieldWithPath("access_token").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("리프레시 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("새 비밀번호 성공")
    void setNewPassword() throws Exception{
        // given
        Map<String, String> body = new HashMap<>();
        body.put("new_password", "test1234");

        // when & then
        mockMvc.perform(post("/api/v1/users/set-new-password")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("users/set-new-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("new_password").description("새 비밀번호")
                        )
                ));
    }

    @Test
    @DisplayName("내가 작성한 게시판 글 목록 조회")
    void getMyPostList() throws Exception {
        List<PostListDto> postList = List.of(new PostListDto(post));
        given(userService.getMyPostList(1,10,user)).willReturn(postList);

        mockMvc.perform(get("/api/v1/users/posts")
                .contentType("application/json")
                .param("size","10")
                .param("page","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].user_id").value(1L))
                .andExpect(jsonPath("$[0].user_nickname").value("테스트"))
                .andExpect(jsonPath("$[0].title").value("타이틀"))
                .andExpect(jsonPath("$[0].category").value("FREE"))
                .andExpect(jsonPath("$[0].content").value("내용"))
                .andExpect(jsonPath("$[0].views").value(1))
                .andExpect(jsonPath("$[0].is_notice").value(false))
                .andExpect(jsonPath("$[0].created_at").value("2023-08-02 12:00:00"))
                .andDo(document("users/my-written-post-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈"),
                                parameterWithName("page").description("페이지")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                                fieldWithPath("[].user_id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                                fieldWithPath("[].user_nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("[].views").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("[].is_notice").type(JsonFieldType.BOOLEAN).description("공지 유무"),
                                fieldWithPath("[].created_at").type(JsonFieldType.STRING).description("작성일")
                        )));
    }


    @Test
    @DisplayName("내가 작성한 공동배달 글 목록 조회")
    void getMyWrittenJointDeliveries() throws Exception {
        List<JointDeliveryListDto> jointDeliveryListDtos = List.of(new JointDeliveryListDto(jointDelivery,10000));
        given(userService.getMyWrittenJointDeliveries(1,10,user)).willReturn(jointDeliveryListDtos);

        mockMvc.perform(get("/api/v1/users/joint-deliveries-writer")
                        .contentType("application/json")
                        .param("size","10")
                        .param("page","1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].current_total_price").value(10000))
                .andExpect(jsonPath("$[0].target_min_price").value(20000))
                .andExpect(jsonPath("$[0].store_id").value(1L))
                .andExpect(jsonPath("$[0].store_name").value("가게명"))
                .andExpect(jsonPath("$[0].store_image").value("storeImage.jpg"))
                .andExpect(jsonPath("$[0].user_id").value(1L))
                .andExpect(jsonPath("$[0].nickname").value("테스트"))
                .andDo(document("users/joint-deliveries-writing",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈"),
                                parameterWithName("page").description("페이지")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("공동배달 ID"),
                                fieldWithPath("[].current_total_price").type(JsonFieldType.NUMBER).description("현재전체금액"),
                                fieldWithPath("[].target_min_price").type(JsonFieldType.NUMBER).description("최소목표금액"),
                                fieldWithPath("[].store_id").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("[].store_name").type(JsonFieldType.STRING).description("가게명"),
                                fieldWithPath("[].store_image").type(JsonFieldType.STRING).description("가게 이미지"),
                                fieldWithPath("[].user_id").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("닉네임")
                        )));
    }

    @Test
    @DisplayName("내가 참여한 공동배달 글 조회")
    void getMyJoinedJointDeliveries() throws Exception {
        // given
        List<JointDeliveryListDto> jointDeliveryListDtos = List.of(new JointDeliveryListDto(jointDelivery,10000));
        given(userService.getMyJoinedJointDeliveries(1,10,user)).willReturn(jointDeliveryListDtos);


        // when & then
        mockMvc.perform(get("/api/v1/users/joint-deliveries-participating")
                        .param("size","10")
                        .param("page","1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].current_total_price").value(10000))
                .andExpect(jsonPath("$[0].target_min_price").value(20000))
                .andExpect(jsonPath("$[0].store_id").value(1L))
                .andExpect(jsonPath("$[0].store_name").value("가게명"))
                .andExpect(jsonPath("$[0].store_image").value("storeImage.jpg"))
                .andExpect(jsonPath("$[0].user_id").value(1L))
                .andExpect(jsonPath("$[0].nickname").value("테스트"))
                .andDo(document("users/joint-deliveries-participating",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("공동배달 ID"),
                                fieldWithPath("[].current_total_price").type(JsonFieldType.NUMBER).description("현재까지 모인 금액"),
                                fieldWithPath("[].target_min_price").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                fieldWithPath("[].store_id").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("[].store_name").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("[].store_image").type(JsonFieldType.STRING).description("가게 이미지"),
                                fieldWithPath("[].user_id").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("유저 닉네임")
                        )
                ));
    }


    @Test
    @DisplayName("내가 작성한 게시판 글 페이지 수 조회")
    void getMyPostsPageCount_success() throws Exception {
        // given
        given(userService.getMyPostsPageCount(any(Integer.class),eq(user))).willReturn(1);

        // when & then
        mockMvc.perform(get("/api/v1/users/page-post")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("users/pageCount/getMyPosts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }

    @Test
    @DisplayName("내가 작성한 공동배달 게시글 페이지 수 조회")
    void getMyWrittenJointDeliveriesPageCount_success() throws Exception {
        // given
        given(userService.getMyWrittenJointDeliveriesPageCount(any(Integer.class),eq(user))).willReturn(1);

        // when & then
        mockMvc.perform(get("/api/v1/users/page-writing")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("users/pageCount/getWrittenJointDelivery",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }

    @Test
    @DisplayName("내가 참여한 공동배달 게시글 페이지 수 조회")
    void getMyJoinedJointDeliveriesPageCount_success() throws Exception {
        // given
        given(userService.getMyJoinedJointDeliveriesPageCount(any(Integer.class),eq(user))).willReturn(0);

        // when & then
        mockMvc.perform(get("/api/v1/users/page-join")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("users/pageCount/MyJoinedJointDeliver",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }

    @Test
    @DisplayName("내가 작성한 중고거래 게시글 페이지 수 조회")
    void getMyTransactionPageCount_success() throws Exception {
        // given
        given(userService.getMyTransactionPageCount(any(Integer.class),eq(user))).willReturn(2);

        // when & then
        mockMvc.perform(get("/api/v1/users/page-transaction")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("users/pageCount/getMyTransaction",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }

    @Test
    @DisplayName("내가 참여한 중고거래 게시글 페이지 수 조회")
    void getMyJoinedTransactionPageCount_success() throws Exception {
        // given
        given(userService.getMyJoinedTransactionPageCount(any(Integer.class),eq(user))).willReturn(2);

        // when & then
        mockMvc.perform(get("/api/v1/users/page-joined-transaction")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("users/pageCount/joined-transaction",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }


    @Test
    @DisplayName("내가 작성한 공동구매 게시글 페이지 수 조회")
    void getMyJointPurchasePageCount() throws Exception {
        // given
        given(userService.getMyJointPurchasePageCount(any(Integer.class),eq(user))).willReturn(1);

        // when & then
        mockMvc.perform(get("/api/v1/users/page-joint-purchase")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("users/pageCount/joinedpurchase",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }

    @Test
    @DisplayName("내가 참여한 공동구매 게시글 페이지 수 조회")
    void getMyJoinedJointPurchasePageCount() throws Exception {
        // given
        given(userService.getMyJoinedJointPurchasePageCount(any(Integer.class),eq(user))).willReturn(1);

        // when & then
        mockMvc.perform(get("/api/v1/users/page-joined-purchase")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("users/pageCount/joined-joined-purchase",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }
}
