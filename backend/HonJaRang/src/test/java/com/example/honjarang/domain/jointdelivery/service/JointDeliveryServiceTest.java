package com.example.honjarang.domain.jointdelivery.service;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.dto.*;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryApplicant;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.exception.*;
import com.example.honjarang.domain.jointdelivery.repository.*;
import com.example.honjarang.domain.map.dto.CoordinateDto;
import com.example.honjarang.domain.map.service.MapService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.InsufficientPointsException;
import com.example.honjarang.domain.user.repository.UserRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
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
    private JointDeliveryApplicantRepository jointDeliveryApplicantRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MapService mapService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    private Store store;
    private User user;
    private Menu menu;
    private JointDelivery jointDelivery;
    private JointDeliveryCart jointDeliveryCart;
    private JointDeliveryApplicant jointDeliveryApplicant;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@test.com")
                .password("test1234")
                .nickname("테스트")
                .point(10000)
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .point(10000)
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
        jointDelivery.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
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

        given(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).willReturn(responseEntity);
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

        given(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).willReturn(responseEntity);
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
    void createJointDelivery_Success() throws JsonProcessingException {
        // given
        JointDeliveryCreateDto jointDeliveryCreateDto = new JointDeliveryCreateDto("테스트 공동배달", 1L, 3000, 10000, "2000-01-01 00:00:00");
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
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(jointDeliveryRepository.save(any(JointDelivery.class))).willReturn(jointDelivery);

        // when
        Long jointDeliveryId = jointDeliveryService.createJointDelivery(jointDeliveryCreateDto, user);

        // then
        assertThat(jointDeliveryId).isNotNull();
        assertThat(jointDeliveryId).isEqualTo(1L);
    }

    @Test
    @DisplayName("공동배달 생성 실패 - 보증금이 부족한 경우")
    public void createJointDelivery_InsufficientPointException() {
        // given
        JointDeliveryCreateDto jointDeliveryCreateDto = new JointDeliveryCreateDto("테스트 공동배달", 1L, 3000, 10000, "2000-01-01 00:00:00");
        user.subtractPoint(10000);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when & then
        assertThrows(InsufficientPointsException.class, () -> jointDeliveryService.createJointDelivery(jointDeliveryCreateDto, user));
    }


    @Test
    @DisplayName("공동배달 상세조회 성공")
    void getJointDelivery_Success() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(List.of(jointDeliveryCart));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));

        // when
        JointDeliveryDto jointDeliveryDto = jointDeliveryService.getJointDelivery(1L, user);

        // then
        assertThat(jointDeliveryDto).isNotNull();
        assertThat(jointDeliveryDto.getId()).isEqualTo(1L);
        assertThat(jointDeliveryDto.getContent()).isEqualTo("테스트 공동배달");
        assertThat(jointDeliveryDto.getDeliveryCharge()).isEqualTo(3000);
        assertThat(jointDeliveryDto.getCurrentTotalPrice()).isEqualTo(10000);
        assertThat(jointDeliveryDto.getTargetMinPrice()).isEqualTo(10000);
        assertThat(jointDeliveryDto.getDeadline()).isEqualTo("2030-01-01 00:00:00");
        assertThat(jointDeliveryDto.getCreatedAt()).isEqualTo("2000-01-01 00:00:00");
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
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.getJointDelivery(1L, user));
    }

    @Test
    @DisplayName("공동배달 상세조회 실패 - 메뉴를 찾을 수 없는 경우")
    void getJointDelivery_MenuNotFoundException() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(List.of(jointDeliveryCart));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.empty());

        // when & then
        assertThrows(MenuNotFoundException.class, () -> jointDeliveryService.getJointDelivery(1L, user));
    }

    @Test
    @DisplayName("공동배달 리스트 조회 성공")
    void getJointDeliveryList_Success() {
        // given
        Page<JointDelivery> jointDeliveryPage = new PageImpl<>(List.of(jointDelivery));
        List<JointDeliveryCart> jointDeliveryCartList = List.of(jointDeliveryCart);

        given(jointDeliveryRepository.findAllByIsCanceledFalseAndDeadlineAfterAndDistanceLessThanOrderByCreatedAtDesc(any(LocalDateTime.class), eq(37.123456), eq(127.123456), any(Pageable.class))).willReturn(jointDeliveryPage);
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(jointDeliveryCartList);
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));

        // when
        List<JointDeliveryListDto> jointDeliveryListDtoList = jointDeliveryService.getJointDeliveryList(1, 10, user);

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

        given(jointDeliveryRepository.findAllByIsCanceledFalseAndDeadlineAfterAndDistanceLessThanOrderByCreatedAtDesc(any(LocalDateTime.class), eq(37.123456), eq(127.123456), any(Pageable.class))).willReturn(jointDeliveryPage);
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(jointDeliveryCartList);
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.empty());

        // when & then
        assertThrows(MenuNotFoundException.class, () -> jointDeliveryService.getJointDeliveryList(1, 10, user));
    }

    @Test
    @DisplayName("공동배달 모집 취소 성공")
    void cancelJointDelivery_Success() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(List.of(jointDeliveryCart));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));

        // when
        jointDeliveryService.cancelJointDelivery(1L, user);

        // then
    }

    @Test
    @DisplayName("공동배달 모집 취소 실패 - 공동배달을 찾을 수 없는 경우")
    void cancelJointDelivery_JointDeliveryNotFoundException() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.cancelJointDelivery(1L, user));
    }

    @Test
    @DisplayName("공동배달 모집 취소 실패 - 접근 권한이 없는 경우")
    void cancelJointDelivery_UnauthorizedJointDeliveryAccessException() {
        // given
        User userForTest = User.builder().build();
        userForTest.setIdForTest(2L);
        jointDelivery.setUserForTest(userForTest);

        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));

        // when & then
        assertThrows(UnauthorizedJointDeliveryAccessException.class, () -> jointDeliveryService.cancelJointDelivery(1L, user));
    }

    @Test
    @DisplayName("공동배달 모집 취소 실패 - 공동배달이 취소된 경우")
    void cancelJointDelivery_JointDeliveryCanceledException() {
        // given
        jointDelivery.cancel();
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));

        // when & then
        assertThrows(JointDeliveryCanceledException.class, () -> jointDeliveryService.cancelJointDelivery(1L, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 목록 조회 성공")
    void getJointDeliveryCartList_Success() {
        // given
        List<JointDeliveryCart> jointDeliveryCartList = List.of(jointDeliveryCart);
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(jointDeliveryCartRepository.findAllByJointDeliveryId(1L)).willReturn(jointDeliveryCartList);
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));

        // when
        List<JointDeliveryCartListDto> jointDeliveryCartListDtoList = jointDeliveryService.getJointDeliveryCartList(1L, user);

        // then
        assertThat(jointDeliveryCartListDtoList).isNotNull();
        assertThat(jointDeliveryCartListDtoList.size()).isEqualTo(1);
        assertThat(jointDeliveryCartListDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(jointDeliveryCartListDtoList.get(0).getMenuId()).isEqualTo("60f0b0b7e0b9a72e7c7b3b3a");
        assertThat(jointDeliveryCartListDtoList.get(0).getMenuName()).isEqualTo("테스트 메뉴");
        assertThat(jointDeliveryCartListDtoList.get(0).getMenuPrice()).isEqualTo(10000);
        assertThat(jointDeliveryCartListDtoList.get(0).getMenuImage()).isEqualTo("test.jpg");
        assertThat(jointDeliveryCartListDtoList.get(0).getQuantity()).isEqualTo(1);
        assertThat(jointDeliveryCartListDtoList.get(0).getUserId()).isEqualTo(1L);
        assertThat(jointDeliveryCartListDtoList.get(0).getUserNickname()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("공동배달 장바구니 목록 조회 실패 - 공동배달을 찾을 수 없는 경우")
    void getJointDeliveryCartList_JointDeliveryNotFoundException() {
        // given
        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.getJointDeliveryCartList(1L, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 목록 조회 실패 - 접근 권한이 없는 경우")
    void getJointDeliveryCartList_JointDeliveryCartAccessException() {
        // given
        User userForTest = User.builder().build();
        userForTest.setIdForTest(2L);
        jointDelivery.setUserForTest(userForTest);

        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(jointDeliveryApplicantRepository.existsByJointDeliveryIdAndUserId(1L, 1L)).willReturn(false);

        // when & then
        assertThrows(JointDeliveryCartAccessException.class, () -> jointDeliveryService.getJointDeliveryCartList(1L, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 추가 성공")
    void addJointDeliveryCart_Success() {
        // given
        JointDeliveryCartCreateDto jointDeliveryCartCreateDto = new JointDeliveryCartCreateDto(1L, "60f0b0b7e0b9a72e7c7b3b3a", 1);

        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        jointDeliveryService.addJointDeliveryCart(jointDeliveryCartCreateDto, user);

        // then
    }

    @Test
    @DisplayName("공동배달 장바구니 추가 실패 - 공동배달을 찾을 수 없는 경우")
    void addJointDeliveryCart_JointDeliveryNotFoundException() {
        // given
        JointDeliveryCartCreateDto jointDeliveryCartCreateDto = new JointDeliveryCartCreateDto(1L, "60f0b0b7e0b9a72e7c7b3b3a", 1);

        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.addJointDeliveryCart(jointDeliveryCartCreateDto, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 추가 실패 - 공동배달이 마감된 경우")
    void addJointDeliveryCart_JointDeliveryExpiredException() {
        // given
        JointDeliveryCartCreateDto jointDeliveryCartCreateDto = new JointDeliveryCartCreateDto(1L, "60f0b0b7e0b9a72e7c7b3b3a", 1);
        jointDelivery.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));

        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));

        // when & then
        assertThrows(JointDeliveryExpiredException.class, () -> jointDeliveryService.addJointDeliveryCart(jointDeliveryCartCreateDto, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 추가 실패 - 공동배달이 취소된 경우")
    void addJointDeliveryCart_JointDeliveryCanceledException() {
        // given
        JointDeliveryCartCreateDto jointDeliveryCartCreateDto = new JointDeliveryCartCreateDto(1L, "60f0b0b7e0b9a72e7c7b3b3a", 1);
        jointDelivery.cancel();

        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));

        // when & then
        assertThrows(JointDeliveryCanceledException.class, () -> jointDeliveryService.addJointDeliveryCart(jointDeliveryCartCreateDto, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 추가 실패 - 포인트가 부족한 경우")
    void addJointDeliveryCart_InsufficientPointsException() {
        // given
JointDeliveryCartCreateDto jointDeliveryCartCreateDto = new JointDeliveryCartCreateDto(1L, "60f0b0b7e0b9a72e7c7b3b3a", 1);
        user.subtractPoint(10000);
        User userForTest = User.builder().build();
        userForTest.setIdForTest(2L);
        jointDelivery.setUserForTest(userForTest);

        given(jointDeliveryRepository.findById(1L)).willReturn(Optional.of(jointDelivery));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when & then
        assertThrows(InsufficientPointsException.class, () -> jointDeliveryService.addJointDeliveryCart(jointDeliveryCartCreateDto, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 삭제 성공")
    void removeJointDeliveryCart_Success() {
        // given
        given(jointDeliveryCartRepository.findById(1L)).willReturn(Optional.of(jointDeliveryCart));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));

        // when
        jointDeliveryService.removeJointDeliveryCart(1L, user);

        // then
    }

    @Test
    @DisplayName("공동배달 장바구니 삭제 실패 - 공동배달을 찾을 수 없는 경우")
    void removeJointDeliveryCart_JointDeliveryNotFoundException() {
        // given
        given(jointDeliveryCartRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(JointDeliveryNotFoundException.class, () -> jointDeliveryService.removeJointDeliveryCart(1L, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 삭제 실패 - 공동배달이 마감된 경우")
    void removeJointDeliveryCart_JointDeliveryExpiredException() {
        // given
        jointDelivery.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        given(jointDeliveryCartRepository.findById(1L)).willReturn(Optional.of(jointDeliveryCart));

        // when & then
        assertThrows(JointDeliveryExpiredException.class, () -> jointDeliveryService.removeJointDeliveryCart(1L, user));
    }

    @Test
    @DisplayName("공동배달 장바구니 삭제 실패 - 공동배달이 취소된 경우")
    void removeJointDeliveryCart_JointDeliveryCanceledException() {
        // given
        jointDelivery.cancel();
        given(jointDeliveryCartRepository.findById(1L)).willReturn(Optional.of(jointDeliveryCart));

        // when & then
        assertThrows(JointDeliveryCanceledException.class, () -> jointDeliveryService.removeJointDeliveryCart(1L, user));
    }

    @Test
    @DisplayName("공동배달 수령 확인 성공")
    void confirmReceived_Success() {
        // given
        jointDelivery.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        given(jointDeliveryApplicantRepository.findByJointDeliveryIdAndUserId(1L, 1L)).willReturn(Optional.of(jointDeliveryApplicant));
        given(jointDeliveryCartRepository.findAllByJointDeliveryIdAndUserId(1L, 1L)).willReturn(List.of(jointDeliveryCart));
        given(menuRepository.findById(new ObjectId("60f0b0b7e0b9a72e7c7b3b3a"))).willReturn(Optional.of(menu));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(jointDeliveryApplicantRepository.countByJointDeliveryId(1L)).willReturn(1);

        // when
        jointDeliveryService.confirmReceived(1L, user);

        // then
    }

    @Test
    @DisplayName("공동배달 수령 확인 실패 - 공동배달 신청자가 아닌 경우")
    void confirmReceived_JointDeliveryApplicantNotFoundException() {
        // given
        given(jointDeliveryApplicantRepository.findByJointDeliveryIdAndUserId(1L, 1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(JointDeliveryApplicantNotFoundException.class, () -> jointDeliveryService.confirmReceived(1L, user));
    }

    @Test
    @DisplayName("공동배달 수령 확인 실패 - 공동배달이 마감되지 않은 경우")
    void confirmReceived_JointDeliveryNotClosedException() {
        // given
        given(jointDeliveryApplicantRepository.findByJointDeliveryIdAndUserId(1L, 1L)).willReturn(Optional.of(jointDeliveryApplicant));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when & then
        assertThrows(JointDeliveryNotClosedException.class, () -> jointDeliveryService.confirmReceived(1L, user));
    }

    @Test
    @DisplayName("공동배달 수령 확인 실패 - 공동배달이 취소된 경우")
    void confirmReceived_JointDeliveryCanceledException() {
        // given
        jointDelivery.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        jointDelivery.cancel();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(jointDeliveryApplicantRepository.findByJointDeliveryIdAndUserId(1L, 1L)).willReturn(Optional.of(jointDeliveryApplicant));

        // when & then
        assertThrows(JointDeliveryCanceledException.class, () -> jointDeliveryService.confirmReceived(1L, user));
    }

    @Test
    @DisplayName("공동배달 수령 확인 실패 - 이미 수령 확인한 경우")
    void confirmReceived_ReceiptAlreadyConfirmedException() {
        // given
        jointDelivery.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        jointDeliveryApplicant.confirmReceived();

        given(jointDeliveryApplicantRepository.findByJointDeliveryIdAndUserId(1L, 1L)).willReturn(Optional.of(jointDeliveryApplicant));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when & then
        assertThrows(JointDeliveryAlreadyReceivedException.class, () -> jointDeliveryService.confirmReceived(1L, user));
    }

    @Test
    @DisplayName("공동배달 페이지 수 조회 성공")
    void getJointDeliveryPageCount_Success() {
        // given
        given(jointDeliveryRepository.countByIsCanceledFalseAndDeadlineAfter(any(LocalDateTime.class))).willReturn(1);

        // when
        Integer jointDeliveryPageCount = jointDeliveryService.getJointDeliveryPageCount(10);

        // then
        assertThat(jointDeliveryPageCount).isEqualTo(1);
    }
}