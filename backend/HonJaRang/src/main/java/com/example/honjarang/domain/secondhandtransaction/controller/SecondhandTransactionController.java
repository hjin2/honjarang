package com.example.honjarang.domain.secondhandtransaction.controller;


import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.secondhandtransaction.dto.SecondhandTranscationCreateDto;
import com.example.honjarang.domain.secondhandtransaction.service.SecondhandTransactionService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/secondhand-transaction")
public class SecondhandTransactionController {

    private final SecondhandTransactionService secondTransactionService;

    @PostMapping("")
    public ResponseEntity<Void> createSecondTransaction(@RequestBody SecondhandTranscationCreateDto secondhandTransactionCreateDto, @CurrentUser User user) {
        secondTransactionService.createSecondhandTransaction(secondhandTransactionCreateDto, user);
        return ResponseEntity.status(201).build();
    }


}
