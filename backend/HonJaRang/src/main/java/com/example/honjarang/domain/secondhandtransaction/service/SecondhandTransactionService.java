package com.example.honjarang.domain.secondhandtransaction.service;

import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.secondhandtransaction.dto.SecondhandTranscationCreateDto;
import com.example.honjarang.domain.secondhandtransaction.repository.SecondhandTransactionRepository;
import com.example.honjarang.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecondhandTransactionService {

    private final SecondhandTransactionRepository secondhandTransactionRepository;

    public void createSecondhandTransaction(SecondhandTranscationCreateDto secondhandTranscationCreateDto, User user) {
        secondhandTransactionRepository.save(secondhandTranscationCreateDto.toEntity(user));
    }
}
