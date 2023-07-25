package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.jointdelivery.exception.JointDeliveryNotFoundException;
import com.example.honjarang.domain.jointdelivery.exception.StoreNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JointDeliveryControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({JointDeliveryNotFoundException.class, StoreNotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}
