package com.example.honjarang.domain.jointpurchase.controller;

import com.example.honjarang.domain.jointpurchase.exception.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JointProductControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
