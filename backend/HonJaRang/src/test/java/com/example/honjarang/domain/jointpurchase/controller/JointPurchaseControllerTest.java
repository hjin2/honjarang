package com.example.honjarang.domain.jointpurchase.controller;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointpurchase.dto.JointPurchaseCreateDto;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.jointpurchase.service.JointPurchaseService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(JointPurchaseController.class)
class JointPurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JointPurchaseService jointPurchaseService;
    private User user;
    private JointPurchase jointPurchase;

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
    void createJointPurchase() throws Exception {
        // given
        JointPurchaseCreateDto jointPurchaseCreateDto = new JointPurchaseCreateDto("테스트 내용", "2030-01-01 00:00:00", 10, "테스트 상품", 10000, 2500, "테스트 장소");


        // when & then
        mockMvc.perform(post("/api/v1/joint-purchases")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(jointPurchaseCreateDto)))
                .andExpect(status().isOk())
                .andDo(document("joint-purchases/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").description("공동구매 내용"),
                                fieldWithPath("deadline").description("공동구매 마감일"),
                                fieldWithPath("target_person_count").description("공동구매 목표 인원"),
                                fieldWithPath("product_name").description("공동구매 상품명"),
                                fieldWithPath("price").description("공동구매 상품 가격"),
                                fieldWithPath("delivery_charge").description("공동구매 배송비"),
                                fieldWithPath("place_keyword").description("공동구매 장소 키워드")
                        )
                ));
    }
}