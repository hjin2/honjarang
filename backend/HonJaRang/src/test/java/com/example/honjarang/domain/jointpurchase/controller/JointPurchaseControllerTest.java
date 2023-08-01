package com.example.honjarang.domain.jointpurchase.controller;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointpurchase.dto.*;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchaseApplicant;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    private JointPurchaseApplicant jointPurchaseApplicant;

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
    @DisplayName("공동구매 생성")
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

    @Test
    @DisplayName("공동구매 모집 취소")
    void cancelJointPurchase() throws Exception {
        // given

        // when & then
        mockMvc.perform(delete("/api/v1/joint-purchases/{jointPurchaseId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("joint-purchases/cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                               parameterWithName("jointPurchaseId").description("공동구매 ID")
                        )
                ));
    }

    @Test
    @DisplayName("공동구매 목록 조회")
    void getJointPurchases() throws Exception {
        // given
        List<JointPurchaseListDto> jointPurchaseListDtos = List.of(new JointPurchaseListDto(jointPurchase, 1));

        given(jointPurchaseService.getJointPurchaseList(1, 10)).willReturn(jointPurchaseListDtos);

        // when & then
        mockMvc.perform(get("/api/v1/joint-purchases")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(document("joint-purchases/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("공동구매 ID"),
                                fieldWithPath("[].product_name").description("공동구매 상품명"),
                                fieldWithPath("[].image").description("공동구매 상품 이미지"),
                                fieldWithPath("[].price").description("공동구매 상품 가격"),
                                fieldWithPath("[].current_person_count").description("공동구매 현재 인원"),
                                fieldWithPath("[].target_person_count").description("공동구매 목표 인원")
                        )
                ));
    }

    @Test
    @DisplayName("공동구매 상세 조회")
    void getJointPurchase() throws Exception {
        // given
        JointPurchaseDto jointPurchaseDto = new JointPurchaseDto(jointPurchase, 1);
        given(jointPurchaseService.getJointPurchase(1L)).willReturn(jointPurchaseDto);

        // when & then
        mockMvc.perform(get("/api/v1/joint-purchases/{jointPurchaseId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("joint-purchases/detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointPurchaseId").description("공동구매 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("공동구매 ID"),
                                fieldWithPath("content").description("공동구매 내용"),
                                fieldWithPath("deadline").description("공동구매 마감일"),
                                fieldWithPath("target_person_count").description("공동구매 목표 인원"),
                                fieldWithPath("product_name").description("공동구매 상품명"),
                                fieldWithPath("image").description("공동구매 상품 이미지"),
                                fieldWithPath("price").description("공동구매 상품 가격"),
                                fieldWithPath("delivery_charge").description("공동구매 배송비"),
                                fieldWithPath("place_name").description("공동구매 장소명"),
                                fieldWithPath("place_latitude").description("공동구매 장소 위도"),
                                fieldWithPath("place_longitude").description("공동구매 장소 경도"),
                                fieldWithPath("created_at").description("공동구매 생성일"),
                                fieldWithPath("user_id").description("공동구매 생성자 ID"),
                                fieldWithPath("nickname").description("공동구매 생성자 닉네임"),
                                fieldWithPath("current_person_count").description("공동구매 현재 인원")
                        )
                ));
    }

    @Test
    @DisplayName("공동구매 신청자 목록 조회")
    void getJointPurchaseApplicants() throws Exception{
        // given
        List<JointPurchaseApplicantListDto> jointPurchaseApplicantListDtos = List.of(new JointPurchaseApplicantListDto(jointPurchaseApplicant));
        given(jointPurchaseService.getJointPurchaseApplicantList(1L)).willReturn(jointPurchaseApplicantListDtos);

        // when & then
        mockMvc.perform(get("/api/v1/joint-purchases/{jointPurchaseId}/applicants", 1L))
                .andExpect(status().isOk())
                .andDo(document("joint-purchases/applicants",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointPurchaseId").description("공동구매 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("공동구매 신청자 ID"),
                                fieldWithPath("[].user_id").description("공동구매 신청자 ID"),
                                fieldWithPath("[].nickname").description("공동구매 신청자 닉네임"),
                                fieldWithPath("[].quantity").description("공동구매 신청 수량"),
                                fieldWithPath("[].total_price").description("공동구매 신청 총 가격"),
                                fieldWithPath("[].is_received").description("공동구매 신청 수령 여부")
                        )
                ));
    }

    @Test
    @DisplayName("공동구매 신청")
    void applyJointPurchase() throws Exception{
        // given
        JointPurchaseApplyDto jointPurchaseApplyDto = new JointPurchaseApplyDto(1L, 1);

        // when & then
        mockMvc.perform(post("/api/v1/joint-purchases/{jointPurchaseId}/applicants", 1L)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(jointPurchaseApplyDto)))
                .andExpect(status().isOk())
                .andDo(document("joint-purchases/apply",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointPurchaseId").description("공동구매 ID")
                        ),
                        requestFields(
                                fieldWithPath("joint_purchase_id").description("공동구매 ID"),
                                fieldWithPath("quantity").description("공동구매 신청 수량")
                        )
                ));
    }

    @Test
    @DisplayName("공동구매 신청 취소")
    void cancelJointPurchaseApplicant() throws Exception {
        // given

        // when & then
        mockMvc.perform(delete("/api/v1/joint-purchases/{jointPurchaseId}/applicants", 1L))
                .andExpect(status().isOk())
                .andDo(document("joint-purchases/applicants/cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointPurchaseId").description("공동구매 ID")
                        )
                ));
    }

    @Test
    @DisplayName("공동구매 수령 확인")
    void confirmReceived() throws Exception{
        // given

        // when & then
        mockMvc.perform(put("/api/v1/joint-purchases/{jointPurchaseId}/received", 1L))
                .andExpect(status().isOk())
                .andDo(document("joint-purchases/received",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jointPurchaseId").description("공동구매 ID")
                        )
                ));
    }
}