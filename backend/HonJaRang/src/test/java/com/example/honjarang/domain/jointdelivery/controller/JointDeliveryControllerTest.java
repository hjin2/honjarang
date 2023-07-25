package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.jointdelivery.dto.CreateJoinDeliveryDto;
import com.example.honjarang.domain.jointdelivery.dto.MenuListDto;
import com.example.honjarang.domain.jointdelivery.dto.StoreListDto;
import com.example.honjarang.domain.jointdelivery.exception.JointDeliveryNotFoundException;
import com.example.honjarang.domain.jointdelivery.exception.StoreNotFoundException;
import com.example.honjarang.domain.jointdelivery.service.JointDeliveryService;
import com.example.honjarang.domain.map.service.MapService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    private final Long TEST_STORE_ID = 1L;
    private final String TEST_STORE_NAME = "테스트";
    private final String TEST_STORE_IMAGE = "테스트";
    private final String TEST_STORE_ADDRESS = "테스트";
    private final String TEST_KEYWORD = "테스트";
    private final Long TEST_JOINT_DELIVERY_ID = 1L;
    private final String TEST_MENU_NAME = "테스트";
    private final Integer TEST_MENU_PRICE = 1000;
    private final String TEST_MENU_IMAGE = "테스트";
    private final String TEST_JOINT_DELIVERY_CONTENT = "테스트";
    private final Integer TEST_JOINT_DELIVERY_DELIVERY_CHARGE = 1000;
    private final Integer TEST_JOINT_TARGET_MIN_PRICE = 10000;
    private User user;

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
    }

    @Test
    @DisplayName("가게 리스트 조회 성공")
    void getStoreList_Success() throws Exception {
        // given
        List<StoreListDto> storeListDtoList = List.of(new StoreListDto(TEST_STORE_ID, TEST_STORE_NAME, TEST_STORE_IMAGE, TEST_STORE_ADDRESS));

        given(jointDeliveryService.getStoreListByApi(TEST_KEYWORD)).willReturn(storeListDtoList);

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/stores")
                        .param("keyword", TEST_KEYWORD))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TEST_STORE_ID))
                .andExpect(jsonPath("$[0].name").value(TEST_STORE_NAME))
                .andExpect(jsonPath("$[0].image").value(TEST_STORE_IMAGE))
                .andExpect(jsonPath("$[0].address").value(TEST_STORE_ADDRESS));
    }

    @Test
    @DisplayName("가게 리스트 조회 실패 - 가게를 찾을 수 없는 경우")
    void getStoreList_StoreNotFoundException() {
        // given
        given(jointDeliveryService.getStoreListByApi(TEST_KEYWORD)).willThrow(new StoreNotFoundException(""));

        // when & then
        assertThrows(StoreNotFoundException.class, () -> jointDeliveryService.getStoreListByApi(TEST_KEYWORD));
    }

    @Test
    @DisplayName("메뉴 리스트 조회 성공")
    void getMenuList_Success() throws Exception{
        // given
        List<MenuListDto> menuListDtoList = List.of(new MenuListDto(TEST_MENU_NAME, TEST_MENU_PRICE, TEST_MENU_IMAGE, TEST_STORE_ID));
        given(jointDeliveryService.getMenuList(TEST_JOINT_DELIVERY_ID)).willReturn(menuListDtoList);

        // when & then
        mockMvc.perform(get("/api/v1/joint-deliveries/{deliveryId}/menus", TEST_JOINT_DELIVERY_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(TEST_MENU_NAME))
                .andExpect(jsonPath("$[0].price").value(TEST_MENU_PRICE))
                .andExpect(jsonPath("$[0].image").value(TEST_MENU_IMAGE))
                .andExpect(jsonPath("$[0].store_id").value(TEST_STORE_ID));
    }

    @Test
    @DisplayName("메뉴 리스트 조회 실패 - 공동배달을 찾을 수 없는 경우")
    void getMenuList_JointDeliveryNotFoundException() {
        // given
        given(jointDeliveryService.getMenuList(TEST_JOINT_DELIVERY_ID)).willThrow(new JointDeliveryNotFoundException(""));

        // when & then
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.getMenuList(TEST_JOINT_DELIVERY_ID));
    }

    @Test
    @DisplayName("공동배달 생성")
    void createJointDelivery() throws Exception{
        // given
        CreateJoinDeliveryDto dto = new CreateJoinDeliveryDto(TEST_JOINT_DELIVERY_CONTENT, TEST_STORE_ID, TEST_JOINT_DELIVERY_DELIVERY_CHARGE, TEST_JOINT_TARGET_MIN_PRICE, LocalDateTime.now());

        // when & then
        mockMvc.perform(post("/api/v1/joint-deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}