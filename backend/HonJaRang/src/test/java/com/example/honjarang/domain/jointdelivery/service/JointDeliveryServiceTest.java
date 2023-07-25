package com.example.honjarang.domain.jointdelivery.service;

import com.example.honjarang.domain.jointdelivery.dto.JointDeliveryCreateDto;
import com.example.honjarang.domain.jointdelivery.dto.MenuListDto;
import com.example.honjarang.domain.jointdelivery.dto.StoreListDto;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.exception.JointDeliveryNotFoundException;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryRepository;
import com.example.honjarang.domain.jointdelivery.repository.MenuRepository;
import com.example.honjarang.domain.jointdelivery.repository.StoreRepository;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JointDeliveryServiceTest {

    @InjectMocks
    private JointDeliveryService jointDeliveryService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private JointDeliveryRepository jointDeliveryRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    private final Long TEST_JOINT_DELIVERY_ID = 1L;
    private final String TEST_JOINT_DELIVERY_CONTENT = "테스트";
    private final Integer TEST_JOINT_DELIVERY_DELIVERY_CHARGE = 1000;
    private final Integer TEST_JOINT_TARGET_MIN_PRICE = 10000;
    private final String TEST_KEYWORD = "테스트";
    private final Long TEST_STORE_ID = 37394887L;
    private final String TEST_STORE_NAME = "테스트";
    private final String TEST_STORE_IMAGE = "이미지";
    private final String TEST_STORE_ADDRESS = "주소";
    private final Long TEST_MENU_ID = 1L;
    private final String TEST_MENU_NAME = "테스트";
    private final Integer TEST_MENU_PRICE = 10000;
    private final String TEST_MENU_IMAGE = "이미지";
    private Store store;
    private User user;

    @BeforeEach
    void setUp() {
        store = Store.builder()
                .storeName(TEST_STORE_NAME)
                .image(TEST_STORE_IMAGE)
                .address(TEST_STORE_ADDRESS)
                .latitude(37.123456)
                .longitude(127.123456)
                .build();
        store.setIdForTest(TEST_STORE_ID);

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
    @DisplayName("가게 리스트 가져오기 성공")
    void getStoreListByApi_Success() throws JsonProcessingException {
        // given
        String responseBody = """
                {
                    "result": {
                        "place": {
                            "list": [
                                {
                                    "id": 37394887,
                                    "name": "테스트",
                                    "thumUrl": "이미지",
                                    "roadAddress": "주소"
                                }
                            ]
                        }
                    }
                }""";

        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        JsonNode jsonNode = new ObjectMapper().readTree(responseEntity.getBody());

        given(restTemplate.getForEntity(any(URI.class), eq(String.class))).willReturn(responseEntity);
        given(objectMapper.readTree(eq(responseEntity.getBody()))).willReturn(jsonNode);

        // when
        List<StoreListDto> storeListDtoList = jointDeliveryService.getStoreListByApi(TEST_KEYWORD);

        // then
        assertEquals(TEST_STORE_ID, storeListDtoList.get(0).getId());
        assertEquals(TEST_STORE_NAME, storeListDtoList.get(0).getName());
        assertEquals(TEST_STORE_IMAGE, storeListDtoList.get(0).getImage());
        assertEquals(TEST_STORE_ADDRESS, storeListDtoList.get(0).getAddress());
    }

    @Test
    @DisplayName("가게 리스트 가져오기 실패 - 게가를 찾을 수 없는 경우")
    void getStoreListByApi_StoreNotFoundException() throws JsonProcessingException {
        // given
        String responseBody = """
                {
                    "result": {
                        "place": {
                            "list": []
                        }
                    }
                }""";

        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        JsonNode jsonNode = new ObjectMapper().readTree(responseEntity.getBody());

        given(restTemplate.getForEntity(any(URI.class), eq(String.class))).willReturn(responseEntity);
        given(objectMapper.readTree(eq(responseEntity.getBody()))).willReturn(jsonNode);

        // when
        List<StoreListDto> storeListDtoList = jointDeliveryService.getStoreListByApi(TEST_KEYWORD);

        // then
        assertEquals(0, storeListDtoList.size());
    }

    @Test
    @DisplayName("메뉴 리스트 가져오기 성공")
    void getMenuList_Success() {
        // given
        JointDelivery jointDelivery = JointDelivery.builder()
                .content(TEST_JOINT_DELIVERY_CONTENT)
                .deliveryCharge(TEST_JOINT_DELIVERY_DELIVERY_CHARGE)
                .deadline(LocalDateTime.now())
                .user(null)
                .store(store)
                .build();

        List<MenuListDto> expectedMenuListDtoList = List.of(
                new MenuListDto(TEST_MENU_NAME, TEST_MENU_PRICE, TEST_MENU_IMAGE, TEST_STORE_ID)
        );
        given(jointDeliveryRepository.findById(TEST_JOINT_DELIVERY_ID)).willReturn(java.util.Optional.ofNullable(jointDelivery));
        given(menuRepository.findAllByStoreId(TEST_STORE_ID)).willReturn(expectedMenuListDtoList);

        // when
        List<MenuListDto> menuListDtoList = jointDeliveryService.getMenuList(TEST_JOINT_DELIVERY_ID);

        // then
        assertEquals(TEST_MENU_NAME, menuListDtoList.get(0).getName());
        assertEquals(TEST_MENU_PRICE, menuListDtoList.get(0).getPrice());
        assertEquals(TEST_MENU_IMAGE, menuListDtoList.get(0).getImage());
        assertEquals(TEST_STORE_ID, menuListDtoList.get(0).getStoreId());
    }

    @Test
    @DisplayName("메뉴 리스트 가져오기 실패 - 공동 배달을 찾을 수 없는 경우")
    void getMenuList_JointDeliveryNotFoundException() {
        // given
        given(jointDeliveryRepository.findById(TEST_JOINT_DELIVERY_ID)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.getMenuList(TEST_JOINT_DELIVERY_ID));
    }

    @Test
    @DisplayName("공동배달 생성 성공")
    void createJointDelivery() throws JsonProcessingException{
        // given
        JointDeliveryCreateDto jointDeliveryCreateDto = new JointDeliveryCreateDto(TEST_JOINT_DELIVERY_CONTENT, TEST_STORE_ID, TEST_JOINT_DELIVERY_DELIVERY_CHARGE, TEST_JOINT_TARGET_MIN_PRICE, LocalDateTime.now());
        String responseBody = """
                {
                    "id": "37394887",
                    "name": "테스트",
                    "imageURL": "이미지",
                    "fullRoadAddress": "주소",
                    "y": 37.123456,
                    "x": 127.123456
                }""";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        JsonNode jsonNode = new ObjectMapper().readTree(responseEntity.getBody());

        String responseBody1 = """
                {
                    "Menu:1": {
                        "name": "테스트",
                        "price": 10000,
                        "images": [
                            "이미지"
                        ]
                    }
                }""";
        ResponseEntity<String> responseEntity1 = ResponseEntity.ok(responseBody1);
        JsonNode jsonNode1 = new ObjectMapper().readTree(responseBody1);

        given(restTemplate.getForEntity(any(String.class), eq(String.class))).willReturn(responseEntity);
        given(objectMapper.readTree(eq(responseEntity.getBody()))).willReturn(jsonNode);
        given(objectMapper.readTree(not(eq(responseEntity.getBody())))).willReturn(jsonNode1);

        // when
        jointDeliveryService.createJointDelivery(jointDeliveryCreateDto, user);

        // then
    }
}