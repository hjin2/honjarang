package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.dto.*;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.exception.JointDeliveryNotFoundException;
import com.example.honjarang.domain.jointdelivery.exception.MenuNotFoundException;
import com.example.honjarang.domain.jointdelivery.exception.StoreNotFoundException;
import com.example.honjarang.domain.jointdelivery.service.JointDeliveryService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .deadline(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"))
                .store(store)
                .user(user)
                .build();
        jointDelivery.setIdForTest(1L);
        jointDelivery.setCreatedAt(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        menu = Menu.builder()
                .name("테스트 메뉴")
                .price(10000)
                .image("test.jpg")
                .storeId(1L)
                .build();
        menu.setIdForTest(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"));
    }

    @Test
    @DisplayName("가게 리스트 조회 성공")
    void getStoreList_Success() throws Exception {
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
                .andExpect(jsonPath("$[0].address").value("서울특별시 강남구"));
    }

    @Test
    @DisplayName("가게 리스트 조회 실패 - 가게를 찾을 수 없는 경우")
    void getStoreList_StoreNotFoundException() throws Exception {
        // given
        given(jointDeliveryService.getStoreListByApi("테스트")).willThrow(new StoreNotFoundException(""));

        // when & then
       mockMvc.perform(get("/api/v1/joint-deliveries/stores")
                        .param("keyword", "테스트"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 리스트 조회 성공")
    void getMenuList_Success() throws Exception{
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
                .andExpect(jsonPath("$[0].image").value("test.jpg"));
    }

    @Test
    @DisplayName("메뉴 리스트 조회 실패 - 공동배달을 찾을 수 없는 경우")
    void getMenuList_JointDeliveryNotFoundException() throws Exception{
        // given
        given(jointDeliveryService.getMenuList(1L)).willThrow(new JointDeliveryNotFoundException(""));

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/{jointDeliveryId}/menus", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("공동배달 생성")
    void createJointDelivery() throws Exception{
        // given
        JointDeliveryCreateDto dto = new JointDeliveryCreateDto("테스트 공동배달", 1L, 3000, 10000, "2000-01-01 00:00:00");

        // when & then
        mockMvc.perform(post("/api/v1/joint-deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dto))
                        .sessionAttr("user", user))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("공동배달 리스트 조회 성공")
    void getJointDeliveryList_Success() throws Exception {
        // given
        List<JointDeliveryListDto> jointDeliveryListDtoList = List.of(new JointDeliveryListDto(jointDelivery, 10000));

        given(jointDeliveryService.getJointDeliveryList(1, 10)).willReturn(jointDeliveryListDtoList);

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
                .andExpect(jsonPath("$[0].nickname").value("테스트"));
    }

    @Test
    @DisplayName("공동배달 리스트 조회 실패 - 메뉴를 찾을 수 없는 경우")
    void getJointDeliveryList_MenuNotFoundException() throws Exception {
        // given
        given(jointDeliveryService.getJointDeliveryList(1, 10)).willThrow(new MenuNotFoundException(""));

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("공동배달 조회 성공")
    void getJointDelivery_Success() throws Exception{
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
                .andExpect(jsonPath("$.nickname").value("테스트"));
    }

    @Test
    @DisplayName("공동배달 조회 실패 - 공동배달을 찾을 수 없는 경우")
    void getJointDelivery_JointDeliveryNotFoundException() throws Exception{
        // given
        given(jointDeliveryService.getJointDelivery(1L)).willThrow(new JointDeliveryNotFoundException(""));

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/{jointDeliveryId}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("공동배달 조회 실패 - 메뉴를 찾을 수 없는 경우")
    void getJointDelivery_MenuNotFoundException() throws Exception{
        // given
        given(jointDeliveryService.getJointDelivery(1L)).willThrow(new MenuNotFoundException(""));

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/{jointDeliveryId}", 1L))
                .andExpect(status().isNotFound());
    }
}