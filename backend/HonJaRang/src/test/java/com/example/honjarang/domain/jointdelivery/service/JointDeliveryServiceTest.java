package com.example.honjarang.domain.jointdelivery.service;

import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.dto.*;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.exception.JointDeliveryNotFoundException;
import com.example.honjarang.domain.jointdelivery.exception.MenuNotFoundException;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryCartRepository;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryRepository;
import com.example.honjarang.domain.jointdelivery.repository.MenuRepository;
import com.example.honjarang.domain.jointdelivery.repository.StoreRepository;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private JointDeliveryCartRepository jointDeliveryCartRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    private Store store;
    private User user;
    private Menu menu;
    private JointDelivery jointDelivery;
    private JointDeliveryCart jointDeliveryCart;

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
                .deadline(LocalDateTime.now().plusHours(1))
                .store(store)
                .user(user)
                .build();
        jointDelivery.setIdForTest(1L);
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
                .build();
        jointDeliveryCart.setIdForTest(1L);
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
                                    "id": 1,
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
        given(objectMapper.readTree(responseEntity.getBody())).willReturn(jsonNode);

        // when
        List<StoreListDto> storeListDtoList = jointDeliveryService.getStoreListByApi("테스트");

        // then
        assertThat(storeListDtoList).isNotNull();
        assertThat(storeListDtoList.size()).isEqualTo(1);
        assertThat(storeListDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(storeListDtoList.get(0).getName()).isEqualTo("테스트");
        assertThat(storeListDtoList.get(0).getImage()).isEqualTo("이미지");
        assertThat(storeListDtoList.get(0).getAddress()).isEqualTo("주소");
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
        given(objectMapper.readTree(responseEntity.getBody())).willReturn(jsonNode);

        // when
        List<StoreListDto> storeListDtoList = jointDeliveryService.getStoreListByApi("테스트");

        // then
        assertThat(storeListDtoList).isNotNull();
        assertThat(storeListDtoList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("메뉴 리스트 가져오기 성공")
    void getMenuList_Success() {
        // given
        List<Menu> menuList = List.of(menu);
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(menuRepository.findAllByStoreId(1L)).willReturn(menuList);

        // when
        List<MenuListDto> menuListDtoList = jointDeliveryService.getMenuList(1L);

        // then
        assertThat(menuListDtoList).isNotNull();
        assertThat(menuListDtoList.size()).isEqualTo(1);
        assertThat(menuListDtoList.get(0).getId()).isEqualTo("60f0b0b7e0b9a72e7c7b3b3a");
        assertThat(menuListDtoList.get(0).getName()).isEqualTo("테스트 메뉴");
        assertThat(menuListDtoList.get(0).getPrice()).isEqualTo(10000);
        assertThat(menuListDtoList.get(0).getImage()).isEqualTo("test.jpg");
    }

    @Test
    @DisplayName("메뉴 리스트 가져오기 실패 - 공동 배달을 찾을 수 없는 경우")
    void getMenuList_JointDeliveryNotFoundException() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.getMenuList(1L));
    }

    @Test
    @DisplayName("공동배달 생성 성공")
    void createJointDelivery() throws JsonProcessingException {
        // given
        JointDeliveryCreateDto jointDeliveryCreateDto = new JointDeliveryCreateDto("테스트 공동배달", 1L, 3000, 10000, LocalDateTime.now().plusHours(1));
        String responseBody = """
                {
                    "id": 1,
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
                        "name": "테스트 메뉴",
                        "price": 10000,
                        "images": [
                            "test.jpg"
                        ]
                    }
                }""";
        ResponseEntity<String> responseEntity1 = ResponseEntity.ok(responseBody1);
        JsonNode jsonNode1 = new ObjectMapper().readTree(responseBody1);

        given(restTemplate.getForEntity(any(String.class), eq(String.class))).willReturn(responseEntity);
        given(objectMapper.readTree(responseEntity.getBody())).willReturn(jsonNode);
        given(objectMapper.readTree(not(eq(responseEntity.getBody())))).willReturn(jsonNode1);

        // when
        jointDeliveryService.createJointDelivery(jointDeliveryCreateDto, user);

        // then
    }

    @Test
    @DisplayName("공동배달 상세조회 성공")
    void getJointDelivery_Success() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(List.of(jointDeliveryCart));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));

        // when
        JointDeliveryDto jointDeliveryDto = jointDeliveryService.getJointDelivery(1L);

        // then
        assertThat(jointDeliveryDto).isNotNull();
        assertThat(jointDeliveryDto.getId()).isEqualTo(1L);
        assertThat(jointDeliveryDto.getContent()).isEqualTo("테스트 공동배달");
        assertThat(jointDeliveryDto.getDeliveryCharge()).isEqualTo(3000);
        assertThat(jointDeliveryDto.getCurrentTotalPrice()).isEqualTo(10000);
        assertThat(jointDeliveryDto.getTargetMinPrice()).isEqualTo(10000);
        assertThat(jointDeliveryDto.getDeadline()).isEqualTo(jointDelivery.getDeadline());
        assertThat(jointDeliveryDto.getCreatedAt()).isEqualTo(jointDelivery.getCreatedAt());
        assertThat(jointDeliveryDto.getStoreId()).isEqualTo(1L);
        assertThat(jointDeliveryDto.getStoreName()).isEqualTo("테스트 가게");
        assertThat(jointDeliveryDto.getStoreImage()).isEqualTo("test.jpg");
        assertThat(jointDeliveryDto.getUserId()).isEqualTo(1L);
        assertThat(jointDeliveryDto.getNickname()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("공동배달 상세조회 실패 - 공동배달을 찾을 수 없는 경우")
    void getJointDelivery_JointDeliveryNotFoundException() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.getJointDelivery(1L));
    }

    @Test
    @DisplayName("공동배달 상세조회 실패 - 메뉴를 찾을 수 없는 경우")
    void getJointDelivery_MenuNotFoundException() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(List.of(jointDeliveryCart));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.empty());

        // when & then
        assertThrows(MenuNotFoundException.class, () -> jointDeliveryService.getJointDelivery(1L));
    }

    @Test
    @DisplayName("공동배달 리스트 조회 성공")
    void getJointDeliveryList_Success() {
        // given
        Page<JointDelivery> jointDeliveryPage = new PageImpl<>(List.of(jointDelivery));
        List<JointDeliveryCart> jointDeliveryCartList = List.of(jointDeliveryCart);

        given(jointDeliveryRepository.findAllByDeadlineAfter(any(LocalDateTime.class), any(Pageable.class))).willReturn(jointDeliveryPage);
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(jointDeliveryCartList);
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));

        // when
        List<JointDeliveryListDto> jointDeliveryListDtoList = jointDeliveryService.getJointDeliveryList(1, 10);

        // then
        assertThat(jointDeliveryListDtoList).isNotNull();
        assertThat(jointDeliveryListDtoList.size()).isEqualTo(1);
        assertThat(jointDeliveryListDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(jointDeliveryListDtoList.get(0).getCurrentTotalPrice()).isEqualTo(10000);
        assertThat(jointDeliveryListDtoList.get(0).getTargetMinPrice()).isEqualTo(10000);
        assertThat(jointDeliveryListDtoList.get(0).getStoreId()).isEqualTo(1L);
        assertThat(jointDeliveryListDtoList.get(0).getStoreName()).isEqualTo("테스트 가게");
        assertThat(jointDeliveryListDtoList.get(0).getStoreImage()).isEqualTo("test.jpg");
        assertThat(jointDeliveryListDtoList.get(0).getUserId()).isEqualTo(1L);
        assertThat(jointDeliveryListDtoList.get(0).getNickname()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("공동배달 리스트 조회 실패 - 메뉴를 찾을 수 없는 경우")
    void getJointDeliveryList_MenuNotFoundException() {
        // given
        Page<JointDelivery> jointDeliveryPage = new PageImpl<>(List.of(jointDelivery));
        List<JointDeliveryCart> jointDeliveryCartList = List.of(jointDeliveryCart);

        given(jointDeliveryRepository.findAllByDeadlineAfter(any(LocalDateTime.class), any(Pageable.class))).willReturn(jointDeliveryPage);
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(jointDeliveryCartList);
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.empty());

        // when & then
        assertThrows(MenuNotFoundException.class, () -> jointDeliveryService.getJointDeliveryList(1, 10));
    }
}