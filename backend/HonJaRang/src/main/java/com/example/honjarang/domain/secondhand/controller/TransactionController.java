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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/secondhand-transactions")
public class TransactionController {
    private final TransactionService transactionService;


        @PostMapping("")
        public ResponseEntity<Long> createSecondHandTransaction(@RequestPart(value = "transaction_image", required = false) MultipartFile transactionImage, @ModelAttribute(name="transactionCreateDto") TransactionCreateDto transactionCreateDto, @CurrentUser User user) {
            Long result = transactionService.createSecondHandTransaction(transactionCreateDto, transactionImage, user);
            return ResponseEntity.status(201).body(result);
        }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateSecondHandTransaction(@RequestPart(value = "transaction_image", required = false) MultipartFile transactionImage, @ModelAttribute TransactionUpdateDto transactionUpdateDto, @PathVariable Long transactionId, @CurrentUser User user) throws IOException {
        transactionService.updateSecondHandTransaction(transactionImage, transactionUpdateDto, user, transactionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteSecondHandTransaction(@PathVariable Long transactionId) {
        transactionService.deleteSecondHandTransaction(transactionId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("")
    public ResponseEntity<List<TransactionListDto>> getSecondHandTransactions(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="size", defaultValue="15") int size, @RequestParam(value="keyword", defaultValue="") String keyword){
            List<TransactionListDto> getTransactions = transactionService.getSecondHandTransactions(page, size, keyword);
            return ResponseEntity.ok(getTransactions);
    }



    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getSecondHandTransaction(@PathVariable Long transactionId){
        TransactionDto transactionDto = transactionService.getSecondHandTransaction(transactionId);
        return ResponseEntity.ok(transactionDto);
    }


    @PutMapping("/{transactionId}/buy")
    public ResponseEntity<Void> buySecondHandTransaction(@PathVariable Long transactionId, @CurrentUser User user){
        transactionService.buySecondHandTransaction(transactionId, user);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{transactionId}/check")
    public ResponseEntity<Void> checkSecondHandTransaction(@PathVariable Long transactionId, @CurrentUser User user){
        transactionService.checkSecondHandTransaction(transactionId,user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Integer> getTransactionPageCount(@RequestParam Integer size,  @RequestParam(value="keyword", defaultValue = "")String keyword) {
        return ResponseEntity.ok(transactionService.getTransactionsPageCount(size, keyword));
    }
}
