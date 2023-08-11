package com.example.honjarang.domain.jointpurchase.service;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointpurchase.dto.*;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchaseApplicant;
import com.example.honjarang.domain.jointpurchase.exception.*;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseApplicantRepository;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseRepository;
import com.example.honjarang.domain.map.service.MapService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.InsufficientPointsException;
import com.example.honjarang.domain.user.repository.UserRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JointPurchaseServiceTest {

    @InjectMocks
    private JointPurchaseService jointPurchaseService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private JointPurchaseRepository jointPurchaseRepository;
    @Mock
    private JointPurchaseApplicantRepository jointPurchaseApplicantRepository;
    @Mock
    private MapService mapService;
    @Mock
    private UserRepository userRepository;
    private User user;
    private JointPurchase jointPurchase;
    private JointPurchaseApplicant jointPurchaseApplicant;

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
        jointPurchase = JointPurchase.builder()
                .productName("테스트 상품")
                .content("테스트 내용")
                .deadline(DateTimeUtils.parseLocalDateTime("2030-01-01 00:00:00"))
                .image("https://test.com/test.jpg")
                .price(10000)
                .deliveryCharge(2500)
                .targetPersonCount(10)
                .placeName("테스트 장소")
                .latitude(37.123456)
                .longitude(127.123456)
                .user(user)
                .build();
        jointPurchase.setIdForTest(1L);
        jointPurchase.setCanceledForTest(false);
        jointPurchase.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        jointPurchaseApplicant = JointPurchaseApplicant.builder()
                .jointPurchase(jointPurchase)
                .user(user)
                .quantity(1)
                .isReceived(false)
                .build();
        jointPurchaseApplicant.setIdForTest(1L);
    }

    @Test
    @DisplayName("공동구매 생성 성공")
    void createJointPurchase_Success() throws JsonProcessingException {
        // given
        JointPurchaseCreateDto jointPurchaseCreateDto = new JointPurchaseCreateDto("테스트 내용", "2030-01-01 00:00:00", 10, "테스트 상품", 10000, 2500,  "테스트 장소", 37.123456, 127.123456);
        String responseBody = """
                {
                    "items": [
                        {
                            "image": "https://test.com/test.jpg"
                        }
                    ],
                    "documents": [
                        {
                            "place_name": "테스트 장소",
                            "x": 127.123456,
                            "y": 37.123456
                        }
                    ]
                }
                """;
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        given(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).willReturn(responseEntity);
        given(objectMapper.readTree(responseEntity.getBody())).willReturn(jsonNode);
        given(jointPurchaseRepository.save(any(JointPurchase.class))).willReturn(jointPurchase);

        // when
        Long jointPurchaseId = jointPurchaseService.createJointPurchase(jointPurchaseCreateDto, user);

        // then
        assertThat(jointPurchaseId).isEqualTo(1L);
    }

    @Test
    @DisplayName("공동구매 모집 취소 성공 ")
    void cancelJointPurchase_Success() {
        // given
        given(jointPurchaseRepository.findById(jointPurchase.getId())).willReturn(java.util.Optional.ofNullable(jointPurchase));

        // when
        jointPurchaseService.cancelJointPurchase(1L, user);

        // then
    }

    @Test
    @DisplayName("공동구매 모집 취소 실패 - 공동구매가 존재하지 않는 경우")
    void cancelJointPurchase_JointPurchaseNotFound() {
        // given
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(JointPurchaseNotFoundException.class, () -> jointPurchaseService.cancelJointPurchase(1L, user));
    }

    @Test
    @DisplayName("공동구매 모집 취소 실패 - 공동구매가 취소된 경우")
    void cancelJointPurchase_JointPurchaseCanceled() {
        // given
        jointPurchase.setCanceledForTest(true);
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(jointPurchase));

        // when & then
        assertThrows(JointPurchaseCanceledException.class, () -> jointPurchaseService.cancelJointPurchase(1L, user));
    }

    @Test
    @DisplayName("공동구매 목록 조회 성공")
    void getJointPurchaseList_Success() {
        // given
        Page<JointPurchase> jointPurchasePage = new PageImpl<>(List.of(jointPurchase));
        given(jointPurchaseRepository.findAllByIsCanceledFalseAndDeadlineAfterAndDistanceLessThanAndTargetPersonCountGreaterThanOrderByCreatedAtDesc(any(LocalDateTime.class),eq(37.123456), eq(127.123456), eq("테스트"), any(Pageable.class))).willReturn(jointPurchasePage);
        given(jointPurchaseApplicantRepository.countByJointPurchaseId(1L)).willReturn(1);

        // when
        List<JointPurchaseListDto> jointPurchaseList = jointPurchaseService.getJointPurchaseList(1, 10, "테스트", user);

        // then
        assertThat(jointPurchaseList).isNotNull();
        assertThat(jointPurchaseList.size()).isEqualTo(1);
        assertThat(jointPurchaseList.get(0).getId()).isEqualTo(1L);
        assertThat(jointPurchaseList.get(0).getProductName()).isEqualTo("테스트 상품");
        assertThat(jointPurchaseList.get(0).getImage()).isEqualTo("https://test.com/test.jpg");
        assertThat(jointPurchaseList.get(0).getPrice()).isEqualTo(10000);
        assertThat(jointPurchaseList.get(0).getCurrentPersonCount()).isEqualTo(1);
        assertThat(jointPurchaseList.get(0).getTargetPersonCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("공동구매 상세 조회 성공")
    void getJointPurchase_Success() {
        // given
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(jointPurchase));
        given(jointPurchaseApplicantRepository.countByJointPurchaseId(1L)).willReturn(1);

        // when
        JointPurchaseDto jointPurchaseDto = jointPurchaseService.getJointPurchase(1L, user);

        // then
        assertThat(jointPurchaseDto).isNotNull();
        assertThat(jointPurchaseDto.getId()).isEqualTo(1L);
        assertThat(jointPurchaseDto.getProductName()).isEqualTo("테스트 상품");
        assertThat(jointPurchaseDto.getContent()).isEqualTo("테스트 내용");
        assertThat(jointPurchaseDto.getDeadline()).isEqualTo("2030-01-01 00:00:00");
        assertThat(jointPurchaseDto.getImage()).isEqualTo("https://test.com/test.jpg");
        assertThat(jointPurchaseDto.getPrice()).isEqualTo(10000);
        assertThat(jointPurchaseDto.getDeliveryCharge()).isEqualTo(2500);
        assertThat(jointPurchaseDto.getCurrentPersonCount()).isEqualTo(1);
        assertThat(jointPurchaseDto.getTargetPersonCount()).isEqualTo(10);
        assertThat(jointPurchaseDto.getPlaceName()).isEqualTo("테스트 장소");
        assertThat(jointPurchaseDto.getPlaceLatitude()).isEqualTo(37.123456);
        assertThat(jointPurchaseDto.getPlaceLongitude()).isEqualTo(127.123456);
        assertThat(jointPurchaseDto.getCreatedAt()).isEqualTo("2000-01-01 00:00:00");
        assertThat(jointPurchaseDto.getUserId()).isEqualTo(1L);
        assertThat(jointPurchaseDto.getNickname()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("공동구매 상세 조회 실패 - 공동구매가 존재하지 않는 경우")
    void getJointPurchase_JointPurchaseNotFound() {
        // given
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(JointPurchaseNotFoundException.class, () -> jointPurchaseService.getJointPurchase(1L, user));
    }

    @Test
    @DisplayName("공동구매 신청자 목록 조회 성공")
    void getJointPurchaseApplicantList_Success() {
        // given
        given(jointPurchaseApplicantRepository.findAllByJointPurchaseId(1L)).willReturn(List.of(jointPurchaseApplicant));

        // when
        List<JointPurchaseApplicantListDto> jointPurchaseApplicantList = jointPurchaseService.getJointPurchaseApplicantList(1L);

        // then
        assertThat(jointPurchaseApplicantList).isNotNull();
        assertThat(jointPurchaseApplicantList.size()).isEqualTo(1);
        assertThat(jointPurchaseApplicantList.get(0).getId()).isEqualTo(1L);
        assertThat(jointPurchaseApplicantList.get(0).getUserId()).isEqualTo(1L);
        assertThat(jointPurchaseApplicantList.get(0).getNickname()).isEqualTo("테스트");
        assertThat(jointPurchaseApplicantList.get(0).getQuantity()).isEqualTo(1);
        assertThat(jointPurchaseApplicantList.get(0).getTotalPrice()).isEqualTo(10000);
        assertThat(jointPurchaseApplicantList.get(0).getIsReceived()).isEqualTo(false);
    }

    @Test
    @DisplayName("공동구매 신청 성공")
    void applyJointPurchase_Success() {
        // given
        user.addPoint(10000);
        JointPurchaseApplyDto jointPurchaseApplyDto = new JointPurchaseApplyDto(1L, 1);
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(jointPurchase));
        given(userRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(user));

        // when
        jointPurchaseService.applyJointPurchase(jointPurchaseApplyDto, user);

        // then
    }

    @Test
    @DisplayName("공동구매 신청 실패 - 공동구매가 존재하지 않는 경우")
    void applyJointPurchase_JointPurchaseNotFoundException() {
        // given
        JointPurchaseApplyDto jointPurchaseApplyDto = new JointPurchaseApplyDto(1L, 1);
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(JointPurchaseNotFoundException.class, () -> jointPurchaseService.applyJointPurchase(jointPurchaseApplyDto, user));
    }

    @Test
    @DisplayName("공동구매 신청 실패 - 공동구매가 취소된 경우")
    void applyJointPurchase_JointPurchaseCanceledException() {
        // given
        JointPurchaseApplyDto jointPurchaseApplyDto = new JointPurchaseApplyDto(1L, 1);
        jointPurchase.setCanceledForTest(true);
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(jointPurchase));

        // when & then
        assertThrows(JointPurchaseCanceledException.class, () -> jointPurchaseService.applyJointPurchase(jointPurchaseApplyDto, user));
    }

    @Test
    @DisplayName("공동구매 신청 실패 - 공동구매가 마감된 경우")
    void applyJointPurchase_JointPurchaseExpiredException() {
        // given
        JointPurchaseApplyDto jointPurchaseApplyDto = new JointPurchaseApplyDto(1L, 1);
        jointPurchase.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(jointPurchase));

        // when & then
        assertThrows(JointPurchaseExpiredException.class, () -> jointPurchaseService.applyJointPurchase(jointPurchaseApplyDto, user));
    }

    @Test
    @DisplayName("공동구매 신청 실패 - 이미 신청한 경우")
    void applyJointPurchase_JointPurchaseAlreadyAppliedException() {
        // given
        JointPurchaseApplyDto jointPurchaseApplyDto = new JointPurchaseApplyDto(1L, 1);
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(jointPurchase));
        given(jointPurchaseApplicantRepository.existsByJointPurchaseIdAndUserId(1L, 1L)).willReturn(true);

        // when & then
        assertThrows(JointPurchaseAlreadyAppliedException.class, () -> jointPurchaseService.applyJointPurchase(jointPurchaseApplyDto, user));
    }

    @Test
    @DisplayName("공동구매 신청 실패 - 포인트가 부족한 경우")
    void applyJointPurchase_InsufficientPointException() {
        // given
        JointPurchaseApplyDto jointPurchaseApplyDto = new JointPurchaseApplyDto(1L, 1);
        user.subtractPoint(10000);
        given(jointPurchaseRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(jointPurchase));
        given(userRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(user));

        // when & then
        assertThrows(InsufficientPointsException.class, () -> jointPurchaseService.applyJointPurchase(jointPurchaseApplyDto, user));

    }

    @Test
    @DisplayName("공동구매 신청 취소 성공")
    void cancelJointPurchaseApplicant_Success() {
        // given
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.ofNullable(jointPurchaseApplicant));
        given(userRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(user));

        // when
        jointPurchaseService.cancelJointPurchaseApplicant(1L, user);

        // then
    }

    @Test
    @DisplayName("공동구매 신청 취소 실패 - 신청자가 존재하지 않는 경우")
    void cancelJointPurchaseApplicant_JointPurchaseApplicantNotFoundException() {
        // given
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(JointPurchaseApplicantNotFoundException.class, () -> jointPurchaseService.cancelJointPurchaseApplicant(1L, user));
    }

    @Test
    @DisplayName("공동구매 신청 취소 실패 - 공동구매가 존재하지 않는 경우")
    void cancelJointPurchaseApplicant_JointPurchaseCanceledException() {
        // given
        jointPurchase.setCanceledForTest(true);
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.ofNullable(jointPurchaseApplicant));

        // when & then
        assertThrows(JointPurchaseCanceledException.class, () -> jointPurchaseService.cancelJointPurchaseApplicant(1L, user));
    }

    @Test
    @DisplayName("공동구매 신청 취소 실패 - 공동구매가 마감된 경우")
    void cancelJointPurchaseApplicant_JointPurchaseExpiredException() {
        // given
        jointPurchase.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.ofNullable(jointPurchaseApplicant));

        // when & then
        assertThrows(JointPurchaseExpiredException.class, () -> jointPurchaseService.cancelJointPurchaseApplicant(1L, user));
    }

    @Test
    @DisplayName("공동구매 수령 확인 성공")
    void confirmReceived_Success() {
        // given
        jointPurchase.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.ofNullable(jointPurchaseApplicant));

        // when
        jointPurchaseService.confirmReceived(1L, user);

        // then
    }

    @Test
    @DisplayName("공동구매 수령 확인 실패 - 신청자가 존재하지 않는 경우")
    void confirmReceived_JointPurchaseApplicantNotFoundException() {
        // given
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(JointPurchaseApplicantNotFoundException.class, () -> jointPurchaseService.confirmReceived(1L, user));
    }

    @Test
    @DisplayName("공동구매 수령 확인 실패 - 공동구매가 취소된 경우")
    void confirmReceived_JointPurchaseCanceledException() {
        // given
        jointPurchase.setCanceledForTest(true);
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.ofNullable(jointPurchaseApplicant));

        // when & then
        assertThrows(JointPurchaseCanceledException.class, () -> jointPurchaseService.confirmReceived(1L, user));
    }

    @Test
    @DisplayName("공동구매 수령 확인 실패 - 공동구매가 마감되지 않은 경우")
    void confirmReceived_JointPurchaseExpiredException() {
        // given
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.ofNullable(jointPurchaseApplicant));

        // when & then
        assertThrows(JointPurchaseNotClosedException.class, () -> jointPurchaseService.confirmReceived(1L, user));
    }

    @Test
    @DisplayName("공동구매 수령 확인 실패 - 이미 수령한 경우")
    void confirmReceived_JointPurchaseAlreadyReceivedException() {
        // given
        jointPurchase.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2000-01-01 00:00:00"));
        jointPurchaseApplicant.confirmReceived();
        given(jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(1L, 1L)).willReturn(java.util.Optional.ofNullable(jointPurchaseApplicant));

        // when & then
        assertThrows(JointPurchaseAlreadyReceivedException.class, () -> jointPurchaseService.confirmReceived(1L, user));
    }

    @Test
    @DisplayName("공동구매 페이지 수 조회 성공")
    void getJointPurchasePageCount_Success() {
        // given
        given(jointPurchaseRepository.countByIsCanceledFalseAndDeadlineAfter(any(LocalDateTime.class))).willReturn(1);

        // when
        Integer jointPurchasePageCount = jointPurchaseService.getJointPurchasePageCount(10);

        // then
        assertThat(jointPurchasePageCount).isEqualTo(1);
    }
}