package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointdelivery.dto.JointDeliveryListDto;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointpurchase.dto.JointPurchaseListDto;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.secondhand.entity.Transaction;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.honjarang.domain.user.entity.Role.ROLE_USER;
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
    private User user2;
    private Post post;
    private PostListDto postListDto;
    private JointDeliveryListDto jointDeliveryListDto;
    private JointDelivery jointDelivery;
    private Transaction transaction;
    private JointPurchase jointPurchase;
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
                .role(ROLE_USER)
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
        user2 = User.builder()
                .email("test2@test.com")
                .password("test12342")
                .nickname("테스트2")
                .point(20000)
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .role(ROLE_USER)
                .build();
        user2.changeProfileImage("test.jpg");
        user2.setIdForTest(2L);
        transaction = Transaction.builder()
                .seller(user)
                .buyer(user2)
                .title("중고거래 제목")
                .content("중고거래 게시글")
                .price(20000)
                .isCompleted(true)
                .isReceived(true)
                .transactionImage("transaction.jpg")
                .build();
        transaction.setIdForTest(2L);
        jointPurchase = JointPurchase.builder()
                .user(user)
                .content("공동구매 내용")
                .deadline(DateTimeUtils.parseLocalDateTime("2023-08-02 12:23:34"))
                .targetPersonCount(5)
                .productName("귤")
                .image("jointpurchase.jpg")
                .price(30000)
                .deliveryCharge(3000)
                .placeName("구미시 인동")
                .latitude(34.567)
                .longitude(45.678)
                .build();
        jointPurchase.setIdForTest(1L);


        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        TokenDto tokenDto = new TokenDto(1L, "access_token", "refresh_token");

        given(userService.login(any(LoginDto.class))).willReturn(user);
        given(tokenService.generateToken(1L, "test@test.com", ROLE_USER)).willReturn(tokenDto);

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1L))
                .andExpect(jsonPath("$.access_token").value("access_token"))
                .andExpect(jsonPath("$.refresh_token").value("refresh_token"))
                .andDo(document("new/users/login",
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
                .andDo(document("new/users/send-verification-code",
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
                .andDo(document("new/users/verify-code",
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/check-nickname")
                        .param("nickname", "닉네임"))
                .andExpect(status().isOk())
                .andDo(document("new/users/check-nickname",
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/check-email")
                        .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andDo(document("new/users/check-email",
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
                .andDo(document("new/users/signup",
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
                .andDo(document("new/users/change-password",
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
                .andDo(document("new/users/change-user-info",
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
                .andDo(document("new/users/payment",
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
                .andDo(document("new/users/withdraw",
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

    @Test
    @DisplayName("회원정보 조회")
    void getUserInfo_success() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto(user);
        given(userService.getUserInfo(1L)).willReturn(userInfoDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/info")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nickname").value("테스트"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.profile_image").value("test.jpg"))
                .andExpect(jsonPath("$.point").value(10000))
                .andExpect(jsonPath("$.address").value("서울특별시 강남구"))
                .andExpect(jsonPath("$.latitude").value(37.123456))
                .andExpect(jsonPath("$.longitude").value(127.123456))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andDo(document("new/users/info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("id").description("사용자 ID")
                        ),
                        responseFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("profile_image").type(JsonFieldType.STRING).description("프로필 이미지"),
                                fieldWithPath("point").type(JsonFieldType.NUMBER).description("포인트"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("role").type(JsonFieldType.STRING).description("역할")
                        )
                ));
    }

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
                .andDo(document("new/users/fcm-token",
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
                .andDo(document("new/users/deleteUser",
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
                .andDo(document("new/users/logout",
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
                .andDo(document("new/users/profile-image",
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

        given(tokenService.generateToken(1L, "test@test.com", ROLE_USER)).willReturn(tokenDto);
        given(tokenService.getUserByToken(anyString())).willReturn(user);

        // when & then
        mockMvc.perform(post("/api/v1/users/refresh")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1L))
                .andExpect(jsonPath("$.access_token").value("access_token"))
                .andExpect(jsonPath("$.refresh_token").value("refresh_token"))
                .andDo(document("new/users/refresh",
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
                .andDo(document("new/users/set-new-password",
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
        List<PostListDto> postList = List.of(new PostListDto(post,2,2));
        given(userService.getMyPostList(1,10,1L)).willReturn(postList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/posts/{userId}",1L)
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
                .andExpect(jsonPath("$[0].like_cnt").value(2))
                .andExpect(jsonPath("$[0].comment_cnt").value(2))
                .andDo(document("new/users/my-written-post-list",
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
                                fieldWithPath("[].created_at").type(JsonFieldType.STRING).description("작성일"),
                                fieldWithPath("[].like_cnt").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("[].comment_cnt").type(JsonFieldType.NUMBER).description("댓글 수")
                        )));
    }


    @Test
    @DisplayName("내가 작성한 공동배달 글 목록 조회")
    void getMyWrittenJointDeliveries() throws Exception {
        List<JointDeliveryListDto> jointDeliveryListDtos = List.of(new JointDeliveryListDto(jointDelivery,10000));
        given(userService.getMyWrittenJointDeliveries(1,10,1L)).willReturn(jointDeliveryListDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/joint-deliveries-writer/{userId}",1L)
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
                .andDo(document("new/users/joint-deliveries-writing",
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
    @DisplayName("내가 작성한 중고거래 글 목록 조회")
    void getMyTransactions() throws Exception {
        // given
        List<TransactionListDto> transactionListDtoList = List.of(new TransactionListDto(transaction));
        given(userService.getMyTransactions(1,10,1L)).willReturn(transactionListDtoList);


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/transaction-writer/{userId}",1L)
                        .param("size","10")
                        .param("page","1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].title").value("중고거래 제목"))
                .andExpect(jsonPath("$[0].is_complete").value(true))
                .andExpect(jsonPath("$[0].price").value(20000))
                .andExpect(jsonPath("$[0].transaction_image").value("transaction.jpg"))
                .andDo(document("new/users/joint-deliveries-participating",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("중고거래 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("중고거래 게시글 제목"),
                                fieldWithPath("[].is_complete").type(JsonFieldType.BOOLEAN).description("중고거래 판매 완료여부"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("[].transaction_image").type(JsonFieldType.STRING).description("사진")
                        )
                ));
    }

    @Test
    @DisplayName("내가 참여하는 중고거래 글 목록 조회")
    void getMyJoinedTransactions() throws Exception {
        // given
        List<TransactionListDto> transactionListDtoList = List.of(new TransactionListDto(transaction));
        given(userService.getMyJoinedTransactions(eq(1),eq(10),any(User.class))).willReturn(transactionListDtoList);


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/transaction-participating",1L)
                        .param("size","10")
                        .param("page","1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].title").value("중고거래 제목"))
                .andExpect(jsonPath("$[0].is_complete").value(true))
                .andExpect(jsonPath("$[0].price").value(20000))
                .andExpect(jsonPath("$[0].transaction_image").value("transaction.jpg"))
                .andDo(document("new/users/joint-transaction-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("중고거래 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("중고거래 게시글 제목"),
                                fieldWithPath("[].is_complete").type(JsonFieldType.BOOLEAN).description("중고거래 판매 완료여부"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("[].transaction_image").type(JsonFieldType.STRING).description("사진")
                        )
                ));
    }

    @Test
    @DisplayName("내가 참여한 공동배달 글 조회")
    void getMyJoinedJointDeliveries() throws Exception {
        // given
        List<JointDeliveryListDto> jointDeliveryListDtos = List.of(new JointDeliveryListDto(jointDelivery,10000));
        given(userService.getMyJoinedJointDeliveries(1,10,user)).willReturn(jointDeliveryListDtos);


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/joint-deliveries-participating")
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
                .andDo(document("new/users/joint-deliveries-participating",
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
    @DisplayName("내가 작성한 공동구매 글 목록 조회")
    void getMyJointPurchase() throws Exception {
        // given
        List<JointPurchaseListDto> jointPurchaseListDtoList = List.of(new JointPurchaseListDto(jointPurchase,2));
        given(userService.getMyJointPurchase(1,10,1L)).willReturn(jointPurchaseListDtoList);


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/jointpurchase-writer/{userId}",1L)
                        .param("size","10")
                        .param("page","1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].product_name").value("귤"))
                .andExpect(jsonPath("$[0].image").value("jointpurchase.jpg"))
                .andExpect(jsonPath("$[0].price").value(30000))
                .andExpect(jsonPath("$[0].current_person_count").value(2))
                .andExpect(jsonPath("$[0].target_person_count").value(5))
                .andDo(document("new/users/joint-purchase-writing",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("공동구매 ID"),
                                fieldWithPath("[].product_name").type(JsonFieldType.STRING).description("공동구매 품명"),
                                fieldWithPath("[].image").type(JsonFieldType.STRING).description("이미지"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("[].current_person_count").type(JsonFieldType.NUMBER).description("현재 모인 참가자 수"),
                                fieldWithPath("[].target_person_count").type(JsonFieldType.NUMBER).description("목표 참가자 수")
                        )
                ));
    }

    @Test
    @DisplayName("내가 참여하는 공동구매 글 목록 조회")
    void getMyJoinedJointPurchase() throws Exception {
        // given
        List<JointPurchaseListDto> jointPurchaseListDtoList = List.of(new JointPurchaseListDto(jointPurchase,2));
        given(userService.getMyJoinedJointPurchase(eq(1),eq(10),any(User.class))).willReturn(jointPurchaseListDtoList);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/jointpurchase-participating", 1L)
                        .param("size", "10")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].product_name").value("귤"))
                .andExpect(jsonPath("$[0].image").value("jointpurchase.jpg"))
                .andExpect(jsonPath("$[0].price").value(30000))
                .andExpect(jsonPath("$[0].current_person_count").value(2))
                .andExpect(jsonPath("$[0].target_person_count").value(5))
                .andDo(document("new/users/joint-purchase-participating",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("공동구매 ID"),
                                fieldWithPath("[].product_name").type(JsonFieldType.STRING).description("공동구매 품명"),
                                fieldWithPath("[].image").type(JsonFieldType.STRING).description("이미지"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("[].current_person_count").type(JsonFieldType.NUMBER).description("현재 모인 참가자 수"),
                                fieldWithPath("[].target_person_count").type(JsonFieldType.NUMBER).description("목표 참가자 수")
                        )
                ));
    }


    @Test
    @DisplayName("내가 작성한 게시판 글 페이지 수 조회")
    void getMyPostsPageCount_success() throws Exception {
        // given
        given(userService.getMyPostsPageCount(10,1L)).willReturn(1);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/users/page-post/{userId}",1L)
                        .param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1))
                 .andDo(document("new/pagecnt/iwrite/board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("사용자 ID")
                        ),
                        queryParameters(
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseBody()
                ));
    }

    @Test
    @DisplayName("내가 작성한 공동배달 게시글 페이지 수 조회")
    void getMyWrittenJointDeliveriesPageCount_success() throws Exception {
        // given
        given(userService.getMyWrittenJointDeliveriesPageCount(10,1L)).willReturn(1);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/users/page-writing/{userId}",1L)
                        .param("size","10"))
                .andExpect(status().isOk())
                .andDo(document("new/pagecnt/joint-delivery-write",
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/page-join")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("new/pagecnt/joint-delivery-join",
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
        given(userService.getMyTransactionPageCount(10,1L)).willReturn(3);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/page-transaction/{userId}",1L)
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("new/pagecnt/transaction-write",
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
        given(userService.getMyJoinedTransactionPageCount(eq(10),any(User.class))).willReturn(2);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/page-joined-transaction")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("new/pagecnt/transaction-join",
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
        given(userService.getMyJointPurchasePageCount(10,1L)).willReturn(3);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/page-joint-purchase/{userId}",1L)
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("new/pagecnt/joint-purchase-write",
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
        given(userService.getMyJoinedJointPurchasePageCount(eq(10),any(User.class))).willReturn(1);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/page-joined-purchase")
                        .contentType("application/json")
                        .param("size","3"))
                .andExpect(status().isOk())
                .andDo(document("new/pagecnt/joint-purchase-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }
}
