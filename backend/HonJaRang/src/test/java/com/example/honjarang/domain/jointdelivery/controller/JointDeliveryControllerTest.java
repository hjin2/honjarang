package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.dto.*;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryApplicant;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.service.JointDeliveryService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(JointDeliveryController.class)
class JointDeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JointDeliveryService jointDeliveryService;
    private User user;
    private Store store;
    private Menu menu;
    private JointDelivery jointDelivery;
    private JointDeliveryCart jointDeliveryCart;
    private JointDeliveryApplicant jointDeliveryApplicant;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
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
        user.setIdForTest(1L);
        store = Store.builder()
                .storeName("테스트 가게")
                .image("test.jpg")
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .build();
        store.setIdForTest(1L);
        jointDelivery = JointDelivery.builder()
                .content("테스트 공동배달")
                .deliveryCharge(3000)
                .targetMinPrice(10000)
                .deadline(DateTimeUtils.parseLocalDateTime("2030-01-01 00:00:00"))
                .store(store)
                .user(user)
                .build();
        jointDelivery.setIdForTest(1L);
        jointDelivery.setCreatedAtForTest(LocalDateTime.now());
        jointDelivery.setCanceledForTest(false);
        menu = Menu.builder()
                .name("테스트 메뉴")
                .price(10000)
                .image("test.jpg")
                .storeId(1L)
                .build();
        menu.setIdForTest(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"));
        jointDeliveryCart = JointDeliveryCart.builder()
                .jointDelivery(jointDelivery)
                .menuId("60f0b0b7e0b9a72e7c7b3b3a")
                .quantity(1)
                .user(user)
                .build();
        jointDeliveryCart.setIdForTest(1L);
        jointDeliveryApplicant = JointDeliveryApplicant.builder()
                .jointDelivery(jointDelivery)
                .user(user)
                .isReceived(false)
                .build();
        jointDeliveryApplicant.setIdForTest(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    @DisplayName("가게 리스트 조회")
    void getStoreList() throws Exception {
        // given
        List<StoreListDto> storeListDtoList = List.of(new StoreListDto(1L, "테스트 가게", "test.jpg", "서울특별시 강남구"));

        given(jointDeliveryService.getStoreListByApi("테스트")).willReturn(storeListDtoList);

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/stores")
                        .param("keyword", "테스트"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("테스트 가게"))
                .andExpect(jsonPath("$[0].image").value("test.jpg"))
                .andExpect(jsonPath("$[0].address").value("서울특별시 강남구"))
                .andDo(document("joint-deliveries/stores",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("keyword").description("검색할 키워드")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("[].image").type(JsonFieldType.STRING).description("가게 이미지"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("가게 주소")
                        )
                ));
    }

    @Test
    @DisplayName("메뉴 리스트 조회")
    void getMenuList() throws Exception {
        // given
        List<MenuListDto> menuListDtoList = List.of(new MenuListDto(menu));
        given(jointDeliveryService.getMenuList(1L)).willReturn(menuListDtoList);

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/{jointDeliveryId}/menus", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("60f0b0b7e0b9a72e7c7b3b3a"))
                .andExpect(jsonPath("$[0].name").value("테스트 메뉴"))
                .andExpect(jsonPath("$[0].price").value(10000))
                .andExpect(jsonPath("$[0].image").value("test.jpg"))
                .andExpect(jsonPath("$[0].store_id").value(1L))
                .andDo(document("joint-deliveries/menus",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointDeliveryId").description("공동배달 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.STRING).description("메뉴 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("[].image").type(JsonFieldType.STRING).description("메뉴 이미지"),
                                fieldWithPath("[].store_id").type(JsonFieldType.NUMBER).description("가게 ID")
                        )
                ));
    }

    @Test
    @DisplayName("공동배달 생성")
    void createJointDelivery() throws Exception {
        // given
        JointDeliveryCreateDto dto = new JointDeliveryCreateDto("테스트 공동배달", 1L, 3000, 10000, "2000-01-01 00:00:00");

        // when & then
        mockMvc.perform(post("/api/v1/joint-deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("joint-deliveries/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공동배달 내용"),
                                fieldWithPath("store_id").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("delivery_charge").type(JsonFieldType.NUMBER).description("배달비"),
                                fieldWithPath("target_min_price").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                fieldWithPath("deadline").type(JsonFieldType.STRING).description("마감 시간")
                        )
                ));
    }

    @Test
    @DisplayName("공동배달 리스트 조회")
    void getJointDeliveryList() throws Exception {
        // given
        List<JointDeliveryListDto> jointDeliveryListDtoList = List.of(new JointDeliveryListDto(jointDelivery, 10000));

        given(jointDeliveryService.getJointDeliveryList(eq(1), eq(10), any(User.class))).willReturn(jointDeliveryListDtoList);

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].current_total_price").value(10000))
                .andExpect(jsonPath("$[0].target_min_price").value(10000))
                .andExpect(jsonPath("$[0].store_id").value(1L))
                .andExpect(jsonPath("$[0].store_name").value("테스트 가게"))
                .andExpect(jsonPath("$[0].store_image").value("test.jpg"))
                .andExpect(jsonPath("$[0].user_id").value(1L))
                .andExpect(jsonPath("$[0].nickname").value("테스트"))
                .andDo(document("joint-deliveries/list",
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
    @DisplayName("공동배달 조회")
    void getJointDelivery() throws Exception {
        // given
        JointDeliveryDto jointDeliveryDto = new JointDeliveryDto(jointDelivery, 10000);
        given(jointDeliveryService.getJointDelivery(1L)).willReturn(jointDeliveryDto);

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/{jointDeliveryId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("테스트 공동배달"))
                .andExpect(jsonPath("$.delivery_charge").value(3000))
                .andExpect(jsonPath("$.current_total_price").value(10000))
                .andExpect(jsonPath("$.target_min_price").value(10000))
                .andExpect(jsonPath("$.deadline").exists())
                .andExpect(jsonPath("$.store_id").value(1L))
                .andExpect(jsonPath("$.store_name").value("테스트 가게"))
                .andExpect(jsonPath("$.store_image").value("test.jpg"))
                .andExpect(jsonPath("$.user_id").value(1L))
                .andExpect(jsonPath("$.nickname").value("테스트"))
                .andExpect(jsonPath("$.created_at").exists())
                .andDo(document("joint-deliveries/detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointDeliveryId").description("공동배달 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("공동배달 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공동배달 내용"),
                                fieldWithPath("delivery_charge").type(JsonFieldType.NUMBER).description("배달비"),
                                fieldWithPath("current_total_price").type(JsonFieldType.NUMBER).description("현재까지 모인 금액"),
                                fieldWithPath("target_min_price").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                fieldWithPath("deadline").type(JsonFieldType.STRING).description("마감 시간"),
                                fieldWithPath("store_id").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("store_name").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("store_image").type(JsonFieldType.STRING).description("가게 이미지"),
                                fieldWithPath("user_id").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("created_at").type(JsonFieldType.STRING).description("생성 시간")
                        )
                ));
    }

    @Test
    @DisplayName("공봉배달 장바구니 목록 조회")
    void getJointDeliveryCartList() throws Exception {
        // given
        List<JointDeliveryCartListDto> jointDeliveryCartListDtoList = List.of(new JointDeliveryCartListDto(jointDeliveryCart, menu));
        given(jointDeliveryService.getJointDeliveryCartList(1L, user)).willReturn(jointDeliveryCartListDtoList);

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/{jointDeliveryId}/carts", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].menu_id").value("60f0b0b7e0b9a72e7c7b3b3a"))
                .andExpect(jsonPath("$[0].menu_name").value("테스트 메뉴"))
                .andExpect(jsonPath("$[0].menu_price").value(10000))
                .andExpect(jsonPath("$[0].menu_image").value("test.jpg"))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[0].user_id").value(1L))
                .andExpect(jsonPath("$[0].user_nickname").value("테스트"))
                .andDo(document("joint-deliveries/carts/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointDeliveryId").description("공동배달 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("공동배달 장바구니 ID"),
                                fieldWithPath("[].menu_id").type(JsonFieldType.STRING).description("메뉴 ID"),
                                fieldWithPath("[].menu_name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("[].menu_price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("[].menu_image").type(JsonFieldType.STRING).description("메뉴 이미지"),
                                fieldWithPath("[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                                fieldWithPath("[].user_id").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("[].user_nickname").type(JsonFieldType.STRING).description("유저 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("공동배달 장바구니 추가")
    void addJointDeliveryCart() throws Exception {
        // given
        JointDeliveryCartCreateDto dto = new JointDeliveryCartCreateDto(1L, "60f0b0b7e0b9a72e7c7b3b3a", 1);

        // when & then
        mockMvc.perform(post("/api/v1/joint-deliveries/{jointDeliveryId}/carts", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("joint-deliveries/carts/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointDeliveryId").description("공동배달 ID")
                        ),
                        requestFields(
                                fieldWithPath("joint_delivery_id").type(JsonFieldType.NUMBER).description("공동배달 ID"),
                                fieldWithPath("menu_id").type(JsonFieldType.STRING).description("메뉴 ID"),
                                fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량")
                        )
                ));
    }

    @Test
    @DisplayName("공동배달 장바구니 삭제")
    void removeJointDeliveryCart() throws Exception {
        // given

        // when & then
        mockMvc.perform(delete("/api/v1/joint-deliveries/{jointDeliveryId}/carts/{jointDeliveryCartId}", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("joint-deliveries/carts/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointDeliveryId").description("공동배달 ID"),
                                parameterWithName("jointDeliveryCartId").description("공동배달 장바구니 ID")
                        )
                ));
    }

    @Test
    @DisplayName("공동배달 취소")
    void cancelJointDelivery() throws Exception {
        //  given

        // when & then
        mockMvc.perform(delete("/api/v1/joint-deliveries/{jointDeliveryId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("joint-deliveries/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointDeliveryId").description("공동배달 ID")
                        )
                ));
    }


    @Test
    @DisplayName("공동배달 수령 확인")
    void confirmReceipt() throws Exception{
        // given

        // when & then
        mockMvc.perform(put("/api/v1/joint-deliveries/{jointDeliveryId}/receive", 1L))
                .andExpect(status().isOk())
                .andDo(document("joint-deliveries/receive",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointDeliveryId").description("공동배달 ID")
                        )
                ));
    }
}