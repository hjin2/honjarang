package com.example.honjarang.domain.secondhand.service;

import com.example.honjarang.domain.secondhand.dto.TransactionCreateDto;
import com.example.honjarang.domain.secondhand.dto.TransactionDto;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.secondhand.dto.TransactionUpdateDto;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.secondhand.exception.TransactionException;
import com.example.honjarang.domain.secondhand.repository.TransactionRepository;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.InsufficientPointsException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;


    @Transactional
    public void createSecondHandTransaction(TransactionCreateDto dto, User user) {
        transactionRepository.save(dto.toEntity(dto,user));
    }

    @Transactional
    public void updateSecondHandTransaction(TransactionUpdateDto dto, User user){
        Transaction transaction = transactionRepository.findById(dto.getId()).orElseThrow(()->new TransactionException("게시글이 없습니다."));
        transaction.update(dto);
        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteSecondHandTransaction(Long transactionId){
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()-> new TransactionException("해당 게시글이 없습니다."));
        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionListDto> getSecondHandTransactions(Integer page, Integer size, String keyword){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<Transaction> transactionList = transactionRepository.findAllByTitleContainingIgnoreCaseOrderByIdDesc(keyword, pageable);
        List<TransactionListDto> transactionListDtos = new ArrayList<>();
        for(Transaction tmp : transactionList){
            TransactionListDto transactionListDto = new TransactionListDto(tmp);
            transactionListDtos.add(transactionListDto);
        }
        return transactionListDtos;
    }

    @Transactional(readOnly = true)
    public TransactionDto getSecondHandTransaction(Long transactionId){
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()->new TransactionException("존재하지 않는 거래입니다."));
        TransactionDto transactionDto = new TransactionDto(transaction);
        return transactionDto;
    }

    @Transactional
    public void buySecondHandTransaction(Long transactionId, User user){
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()->new TransactionException("존재하지 않는 거래입니다."));
        if(user.getPoint()<transaction.getPrice()){
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }
        User loginedUser = userRepository.findById(user.getId()).orElseThrow(()->new UserNotFoundException("존재하지 않는 사용자입니다."));
        loginedUser.subtractPoint(transaction.getPrice());
        transaction.soldout(user);
        transaction.complete();
    }

    @Transactional
    public void checkSecondHandTransaction(Long transactionId, User user){
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()->new TransactionException("존재하지 않는 거래입니다."));
        transaction.receive();
        User seller = userRepository.findById(transaction.getSeller().getId()).orElseThrow(()->new UserNotFoundException("존재하지 않는 사용자입니다."));
        seller.addPoint(transaction.getPrice());

    }



}
