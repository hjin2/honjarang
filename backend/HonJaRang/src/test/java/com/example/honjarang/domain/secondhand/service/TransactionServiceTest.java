package com.example.honjarang.domain.secondhand.service;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.secondhand.dto.TransactionCreateDto;
import com.example.honjarang.domain.secondhand.dto.TransactionDto;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.secondhand.dto.TransactionUpdateDto;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.secondhand.exception.TransactionException;
import com.example.honjarang.domain.secondhand.repository.TransactionRepository;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.InsufficientPointsException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.google.type.DateTime;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    private User user;

    private Transaction transaction;

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
        transaction.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2023-08-07 13:37:33"));

    }


//    @Test
//    @DisplayName("중고거래 게시글 작성 성공")
//    void createSecondHandTransaction_success() {
//        // given
//        TransactionCreateDto dto = new TransactionCreateDto("중고거래 테스트 제목", "중고거래 테스트 내용", 10000);
//        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction);
//
//        // when
//        Long transactionId = transactionService.createSecondHandTransaction(dto, user);
//
//        // then
//        assertThat(transactionId).isEqualTo(1L);
//    }

//    @Test
//    @DisplayName("중고거래 게시글 수정 성공")
//    void updateSecondHandTransaction_success() {
//        // given
//        TransactionUpdateDto transactionUpdateDto = new TransactionUpdateDto(1L, "수정된 타이틀", "수정된 컨텐츠", 20000);
//        given(transactionRepository.findById(1L)).willReturn(Optional.ofNullable(transaction));
//
//        // when
//        transactionService.updateSecondHandTransaction(transactionUpdateDto, user);
//
//        // then
//        assertThat(transaction.getTitle()).isEqualTo("수정된 타이틀");
//        assertThat(transaction.getContent()).isEqualTo("수정된 컨텐츠");
//        assertThat(transaction.getPrice()).isEqualTo(20000);
//
//    }

//    @Test
//    @DisplayName("중고거래 게시글 수정 실패 - 게시글이 없는 경우")
//    void updateSecondHandTransaction_TransactionException() {
//        // given
//        TransactionUpdateDto transactionUpdateDto = new TransactionUpdateDto(1L, "수정된 타이틀", "수정된 컨텐츠", 20000);
//        given(transactionRepository.findById(1L)).willReturn(java.util.Optional.empty());
//
//        // when & then
//        assertThrows(TransactionException.class, () -> transactionService.updateSecondHandTransaction(transactionUpdateDto, user));
//    }

    @Test
    @DisplayName("중고거래 게시글 삭제 성공")
    void deleteSecondHandTransaction() {
        // given
        given(transactionRepository.findById(1L)).willReturn(Optional.ofNullable(transaction));

        // when & then
        transactionService.deleteSecondHandTransaction(transaction.getId());
    }

    @Test
    @DisplayName("중고거래 게시글 삭제 실패 - 게시글이 없는 경우")
    void deleteSecondHandTransaction_TransactionException() {
        // given
        given(transactionRepository.findById(1L)).willReturn(java.util.Optional.empty());

        // then
        assertThrows(TransactionException.class, () -> transactionService.deleteSecondHandTransaction(transaction.getId()));
    }

    @Test
    @DisplayName("중고거래 게시글 목록 조회 성공")
    void getSecondHandTransactions() {
        // given
        List<Transaction> transactionList = List.of(transaction);
        given(transactionRepository.findAllByTitleContainingIgnoreCaseAndIsCompletedFalseOrderByIdDesc(any(String.class), any(Pageable.class))).willReturn(transactionList);

        // when
        List<TransactionListDto> result = transactionService.getSecondHandTransactions(1, 15, "");

        // then
        assertThat(result.get(0).getTitle()).isEqualTo(transaction.getTitle());
        assertThat(result.get(0).getIsComplete()).isEqualTo(transaction.getIsCompleted());
        assertThat(result.get(0).getPrice()).isEqualTo(transaction.getPrice());

    }

//    @Test
//    @DisplayName("중고거래 게시글 상세조회 성공")
//    void getSecondHandTransaction() {
//        // given
//        given(transactionRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(transaction));
//        TransactionDto transactionDto = new TransactionDto(transaction);
//
//
//        // when
//        TransactionDto result = transactionService.getSecondHandTransaction(1L);
//
//        // then
//        assertThat(transactionDto.getId()).isEqualTo(result.getId());
//        assertThat(transactionDto.getSellerId()).isEqualTo(result.getSellerId());
//        assertThat(transactionDto.getTitle()).isEqualTo(result.getTitle());
//        assertThat(transactionDto.getContent()).isEqualTo(result.getContent());
//        assertThat(transactionDto.getPrice()).isEqualTo(result.getPrice());
//        assertThat(transactionDto.getIsCompleted()).isEqualTo(result.getIsCompleted());
//        assertThat(transactionDto.getCreatedAt()).isEqualTo(result.getCreatedAt());
//    }

    @Test
    @DisplayName("중고거래 게시글 상세조회 실패 - 게시글이 없는 경우")
    void getSecondHandTransaction_TransactionException() {
        // given
        given(transactionRepository.findById(any(Long.class))).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(TransactionException.class, () -> transactionService.getSecondHandTransaction(1L));
    }

    @Test
    @DisplayName("중고거래 구매하기 성공")
    void buySecondHandTransaction_success() {
        // given
        given(transactionRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(transaction));
        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(user));


        // when
        transactionService.buySecondHandTransaction(1L, user);

        // then
        assertThat(user.getPoint()).isEqualTo(5000);
        assertThat(transaction.getBuyer()).isEqualTo(user);
        assertThat(transaction.getIsCompleted()).isEqualTo(true);

    }

    @Test
    @DisplayName("중고거래 구매하기 실패 - 사용자의 포인트가 부족한 경우")
    void buySecondHandTransaction_InsufficientPointsException() {
        // given
        transaction.setPriceForTest(10000);
        user.setPointForTest(5000);
        given(transactionRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(transaction));

        // then
        assertThrows(InsufficientPointsException.class, () -> transactionService.buySecondHandTransaction(1L, user));
    }

    @Test
    @DisplayName("중고거래 구매하기 실패 - 사용자가 존재하지 않는 경우")
    void buySecondHandTransaction_UserNotFoundException() {
        // given
        given(transactionRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(transaction));

        // then
        assertThrows(UserNotFoundException.class, () -> transactionService.buySecondHandTransaction(1L, user));
    }

    @Test
    @DisplayName("중고거래 수령확인 성공")
    void checkSecondHandTransaction_success() {
        // given
        given(transactionRepository.findById(any(Long.class))).willReturn(Optional.of(transaction));
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        int before = user.getPoint();

        // when
        transactionService.checkSecondHandTransaction(1L, user);

        // then
        assertThat(transaction.getIsReceived()).isEqualTo(true);
        assertThat(user.getPoint()).isEqualTo(transaction.getPrice() + before);
    }


    @Test
    @DisplayName("중고거래 수령확인 실패 - 존재하지 않는 거래인 경우")
    void checkSecondHandTransaction_TransactionException() {
        // given
        given(transactionRepository.findById(any(Long.class))).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(TransactionException.class, () -> transactionService.checkSecondHandTransaction(1L, user));

    }

    @Test
    @DisplayName("중고거래 수령확인 실패 - 존재하지 않는 사용자인 경우")
    void checkSecondHandTransaction_UserNotFoundException() {
        // given
        given(transactionRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(transaction));
        given(userRepository.findById(transaction.getSeller().getId())).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> transactionService.checkSecondHandTransaction(1L, user));

    }


}
