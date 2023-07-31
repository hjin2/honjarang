package com.example.honjarang.domain.jointpurchase.service;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointpurchase.dto.JointPurchaseCreateDto;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseRepository;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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

    private User user;
    private JointPurchase jointPurchase;

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
    }

    @Test
    @DisplayName("공동구매 생성 성공")
    void createJointPurchase_Success() throws JsonProcessingException {
        // given
        JointPurchaseCreateDto jointPurchaseCreateDto = new JointPurchaseCreateDto("테스트 내용", "2030-01-01 00:00:00", 10, "테스트 상품", 10000, 2500,  "테스트 장소");
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

        // when
        jointPurchaseService.createJointPurchase(jointPurchaseCreateDto, user);

        // then

    }
}