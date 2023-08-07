package com.example.honjarang.domain.secondhand.service;

import com.example.honjarang.domain.secondhand.dto.TransactionCreateDto;
import com.example.honjarang.domain.secondhand.dto.TransactionUpdateDto;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.secondhand.repository.TransactionRepository;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;
    private User user;
    @Mock
    private Transaction transaction;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .email("test@test.com")
                .password("test1234")
                .nickname("테스트")
                .point(10000)
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .role(Role.ROLE_USER)
                .build();
        user.setIdForTest(1L);
        User buyer = User.builder()
                .email("buyer@test.com")
                .password("testBuy1234")
                .nickname("구매자 테스트")
                .point(30000)
                .address("대구광역시 달서구")
                .latitude(31.123456)
                .longitude(73.123456)
                .role(Role.ROLE_USER)
                .build();
        buyer.setIdForTest(1L);
        transaction = Transaction.builder()
                .seller(user)
                .buyer(buyer)
                .title("타이틀")
                .content("내용")
                .price(5000)
                .isCompleted(false)
                .isReceived(false)
                .build();
        transaction.setIdForTest(1L);

    }


    @Test
    @DisplayName("중고거래 게시글 작성 성공")
    void createSecondHandTransaction_success(){
        // given
        TransactionCreateDto dto = new TransactionCreateDto("중고거래 테스트 제목", "중고거래 테스트 내용", 10000);

        transactionService.createSecondHandTransaction(dto,user);
    }



}
