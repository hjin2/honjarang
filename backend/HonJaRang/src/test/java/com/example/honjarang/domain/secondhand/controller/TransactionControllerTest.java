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
import com.google.api.client.json.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private User user;
    private User buyer;
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

        buyer = User.builder()
                .email("buyertest@test.com")
                .build();
        buyer.setIdForTest(2L);

        transaction = Transaction.builder()
                .seller(user)
                .title("중고거래 타이틀")
                .content("중고거래 내용")
                .price(20000)
                .isCompleted(true)
                .isReceived(false)
                .buyer(buyer)
                .transactionImage("image.jpg")
                .build();
        transaction.setIdForTest(1L);
        transaction.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2023-08-06 12:00:00"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }


    @Test
    @DisplayName("중고거래 게시글 작성")
    void createSecondHandTransaction() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("profile_image", "test.jpg", "image/jpeg", "test".getBytes());
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto("타이틀", "내용", 30000);
        given(transactionService.createSecondHandTransaction(any(TransactionCreateDto.class), any(MockMultipartFile.class), any(User.class))).willReturn(1L);

        // when & then
        mockMvc.perform(multipart("/api/v1/secondhand-transactions")
                        .file("transaction_image", file.getBytes())
                        .part(new MockPart("title","타이틀".getBytes(StandardCharsets.UTF_8)))
                        .part(new MockPart("content","내용".getBytes(StandardCharsets.UTF_8)))
                        .part(new MockPart("price","30000".getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$").value(1L))
                .andDo(document("transaction/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("transaction_image").description("이미지 첨부"),
                                partWithName("title").description("타이틀"),
                                partWithName("content").description("내용"),
                                partWithName("price").description("가격")
                ),
                        responseBody()
                ));
    }

    @Test
    @DisplayName("중고거래 게시글 수정")
    void updateSecondHandTransaction() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("transaction_image", "test.jpg", "image/jpeg", "test".getBytes());
        TransactionUpdateDto transactionUpdateDto = new TransactionUpdateDto("타이틀", "내용", 30000);


        mockMvc.perform(MockMvcRequestBuilders
                .multipart(PUT,"/api/v1/secondhand-transactions/{transactionId}",1L)
                .file("transaction_image", file.getBytes())
                .part(new MockPart("title","타이틀".getBytes(StandardCharsets.UTF_8)))
                .part(new MockPart("content","내용".getBytes(StandardCharsets.UTF_8)))
                .part(new MockPart("price","30000".getBytes(StandardCharsets.UTF_8))))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("transaction/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("transaction_image").description("이미지"),
                                partWithName("title").description("타이틀"),
                                partWithName("content").description("내용"),
                                partWithName("price").description("가격")
                        )
                        ));

    }

    @Test
    @DisplayName("중고거래 게시글 삭제")
    void deleteSecondHandTransaction() throws Exception {

        mockMvc.perform(delete("/api/v1/secondhand-transactions/{transactionId}", 1L))
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
        given(transactionService.getSecondHandTransactions(1, 15, "")).willReturn(transactionListDtos);

        mockMvc.perform(get("/api/v1/secondhand-transactions")
                        .contentType("application/json")
                        .param("page", "1")
                        .param("size", "15")
                        .param("keyword", ""))
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
                                fieldWithPath("[].id").description("중고거래 ID"),
                                fieldWithPath("[].title").description("중고거래 타이틀"),
                                fieldWithPath("[].is_complete").description("판매완료 유무"),
                                fieldWithPath("[].price").description("가격"),
                                fieldWithPath("[].transaction_image").description("중고거래 이미지")
                        ))
                );
    }

    @Test
    @DisplayName("중고거래 게시글 상세 조회")
    void getSecondHandTransaction() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto(transaction,2L);
        given(transactionService.getSecondHandTransaction(1L)).willReturn(transactionDto);

        mockMvc.perform(get("/api/v1/secondhand-transactions/{transactionId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.seller_id").value(1L))
                .andExpect(jsonPath("$.seller_nickname").value("testNickname"))
                .andExpect(jsonPath("$.title").value("중고거래 타이틀"))
                .andExpect(jsonPath("$.content").value("중고거래 내용"))
                .andExpect(jsonPath("$.price").value(20000))
                .andExpect(jsonPath("$.is_completed").value(true))
                .andExpect(jsonPath("$.is_received").value(false))
                .andExpect(jsonPath("$.created_at").value("2023-08-06 12:00:00"))
                .andExpect(jsonPath("$.transaction_image").value("https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/transactionImage/image.jpg"))
                .andExpect(jsonPath("$.buyer_id").value(2L))
                .andDo(document("transaction/detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("transactionId").description("거래 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("거래 ID"),
                                fieldWithPath("seller_id").type(JsonFieldType.NUMBER).description("판매자"),
                                fieldWithPath("seller_nickname").type(JsonFieldType.STRING).description("판매자 닉네임"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("타이틀"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("is_completed").type(JsonFieldType.BOOLEAN).description("판매 유무"),
                                fieldWithPath("created_at").type(JsonFieldType.STRING).description("작성 날짜"),
                                fieldWithPath("transaction_image").type(JsonFieldType.STRING).description("거래상품 이미지"),
                                fieldWithPath("is_received").type(JsonFieldType.BOOLEAN).description("수령여부"),
                                fieldWithPath("buyer_id").type(JsonFieldType.NUMBER).description("구매자 ID")
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
        mockMvc.perform(put("/api/v1/secondhand-transactions/{transactionId}/check", 1L))
                .andDo(document("transaction/receipt",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("transactionId").description("거래 ID")
                        ))
                );
    }

    @Test
    @DisplayName("중고거래 페이지 수")
    void getTransactionPageCount() throws Exception {
        //given
        given(transactionService.getTransactionsPageCount(3,"")).willReturn(10);

        // when&then
        mockMvc.perform(get("/api/v1/secondhand-transactions/page")
                .param("size","3")
                .param("keyword",""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10))
                .andDo(document("transaction/pageCount",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("사이즈"),
                                parameterWithName("keyword").description("키워드")
                        )
                ));
    }



}
