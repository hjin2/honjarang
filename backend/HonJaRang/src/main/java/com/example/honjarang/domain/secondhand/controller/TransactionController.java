package com.example.honjarang.domain.secondhand.controller;

import com.example.honjarang.domain.secondhand.dto.TransactionCreateDto;
import com.example.honjarang.domain.secondhand.dto.TransactionDto;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.secondhand.dto.TransactionUpdateDto;
import com.example.honjarang.domain.secondhand.service.TransactionService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/secondhand-transaction")
public class TransactionController {
    private final TransactionService transactionService;


    @PostMapping("")
    public ResponseEntity<Void> createSecondHandTransaction(@RequestBody TransactionCreateDto dto, @CurrentUser User user) {
        transactionService.createSecondHandTransaction(dto, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateSecondHandTransaction(@PathVariable Long transactionId, @RequestBody TransactionUpdateDto transactionUpdateDto, @CurrentUser User user) {
        transactionService.updateSecondHandTransaction(transactionUpdateDto, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteSecondHandTransaction(@PathVariable Long transactionId) {
        transactionService.deleteSecondHandTransaction(transactionId);
        return ResponseEntity.ok().build();
    }

    @Transactional(readOnly = true)
    @GetMapping("")
    public ResponseEntity<List<TransactionListDto>> getSecondHandTransactions(@RequestParam(value="page", defaultValue="15") int page, @RequestParam(value="size", defaultValue="0") int size, @RequestParam(value="keyword", defaultValue="") String keyword){
            List<TransactionListDto> getTransactions = transactionService.getSecondHandTransactions(page, size, keyword);
            return ResponseEntity.ok(getTransactions);
    }


    @Transactional(readOnly = true)
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getSecondHandTransaction(@PathVariable Long transactionId){
        TransactionDto transactionDto = transactionService.getSecondHandTransaction(transactionId);
        return ResponseEntity.ok(transactionDto);
    }

    @Transactional
    @PutMapping("/buy/{transactionId}")
    public ResponseEntity<Void> buySecondHandTransaction(@PathVariable Long transactionId, @CurrentUser User user){
        transactionService.buySecondHandTransaction(transactionId, user);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping("/check/{transactionId}")
    public ResponseEntity<Void> checkSecondHandTransaction(@PathVariable Long transactionId, @CurrentUser User user){
        transactionService.checkSecondHandTransaction(transactionId,user);
        return ResponseEntity.ok().build();
    }
}
