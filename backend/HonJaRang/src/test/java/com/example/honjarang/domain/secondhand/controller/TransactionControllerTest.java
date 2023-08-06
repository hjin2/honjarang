package com.example.honjarang.domain.secondhand.controller;


import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.secondhand.dto.TransactionCreateDto;
import com.example.honjarang.domain.secondhand.dto.TransactionDto;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.secondhand.dto.TransactionUpdateDto;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.secondhand.service.TransactionService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;



    private User user;
    private Transaction transaction;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("honjarang.kro.kr")
                        .withPort(80))
                .build();
        user = User.builder()
                .email("test@test.com")
                .password("testPassword")
                .nickname("testNickname")
                .point(10000)
                .address("서울특별시 강남구")
                .latitude(34.5678)
                .longitude(56.7890)
                .role(Role.ROLE_USER)
                .profileImage("test.jpg")
                .isDeleted(false)
                .build();
        user.setIdForTest(1L);
        user.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2023-01-01 12:00:00"));

        transaction = Transaction.builder()
                .seller(user)
                .title("중고거래 타이틀")
                .content("중고거래 내용")
                .price(20000)
                .isCompleted(false)
                .isReceived(false)
                .build();
        transaction.setIdForTest(1L);
        transaction.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2023-08-06 12:00:00"));
    }


    @Test
    @DisplayName("중고거래 게시글 작성")
    void createSecondHandTransaction() throws Exception {
        // given
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto("타이틀", "내용", 30000);


        // when & then
        mockMvc.perform(post("/api/v1/secondhand-transactions")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(transactionCreateDto)))
                .andExpect(status().isOk())
                .andDo(document("transaction/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("타이틀"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격")
                        )
                ));

    }


    @Test
    @DisplayName("중고거래 게시글 수정")
    void updateSecondHandTransaction() throws Exception {
        TransactionUpdateDto transactionUpdateDto = new TransactionUpdateDto(1L,"타이틀","내용",30000);

        mockMvc.perform(put("/api/v1/secondhand-transactions/{transactionId}",1L)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(transactionUpdateDto)))
                .andExpect(status().isOk())
                .andDo(document("transaction/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("transactionId").description("거래 ID")
                        ),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("거래 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("거래 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("거래 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격")
                        )
                        ));
    }

    @Test
    @DisplayName("중고거래 게시글 삭제")
    void deleteSecondHandTransaction() throws Exception {

        mockMvc.perform(delete("/api/v1/secondhand-transactions/{transactionId}",1L))
                .andExpect(status().isOk())
                .andDo(document("transaction/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("transactionId").description("거래 ID")
                        )
        ));
    }

    @Test
    @DisplayName("중고거래 게시글 목록 조회")
    void getSecondHandTransactions() throws Exception {

        // given
        List<TransactionListDto> transactionListDtos = List.of(new TransactionListDto(transaction));
        given(transactionService.getSecondHandTransactions(1,15,"")).willReturn(transactionListDtos);

        mockMvc.perform(get("/api/v1/secondhand-transactions")
                .contentType("application/json")
                .param("page","1")
                .param("size","15")
                .param("keyword",""))
                .andExpect(status().isOk())
                .andDo(document("transaction/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈"),
                                parameterWithName("keyword").description("키워드")
                        ),
                        responseFields(
                                fieldWithPath("[].title").description("중고거래 타이틀"),
                                fieldWithPath("[].is_complete").description("판매완료 유무"),
                                fieldWithPath("[].price").description("가격")
                        ))
                );
    }

    @Test
    @DisplayName("중고거래 게시글 상세 조회")
    void getSecondHandTransaction() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto(transaction);
        given(transactionService.getSecondHandTransaction(1L)).willReturn(transactionDto);

        mockMvc.perform(get("/api/v1/secondhand-transactions/{transactionId}",1L))
                .andExpect(status().isOk())
                .andDo(document("transaction/detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("transactionId").description("거래 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("거래 ID"),
                                fieldWithPath("seller_id").type(JsonFieldType.NUMBER).description("판매자"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("타이틀"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("is_completed").type(JsonFieldType.BOOLEAN).description("판매 유무"),
                                fieldWithPath("created_at").type(JsonFieldType.STRING).description("작성 날짜")
                        )
                        ));
    }


    @Test
    @DisplayName("중고거래 구매")
    void buySecondHandTransaction() throws Exception {
        mockMvc.perform(put("/api/v1/secondhand-transactions/{transactionId}/buy", 1L))
                .andExpect(status().isOk())
                .andDo(document("transaction/buy",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("transactionId").description("거래 ID")
                        )));
    }

    @Test
    @DisplayName("중고거래 상품 수령확인")
    void checkSecondHandTransaction() throws Exception {
        mockMvc.perform(put("/api/v1/secondhand-transactions/{transactionId}/check",1L))
                .andDo(document("transaction/receipt",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("transactionId").description("거래 ID")
                        ))
                );
    }



}
