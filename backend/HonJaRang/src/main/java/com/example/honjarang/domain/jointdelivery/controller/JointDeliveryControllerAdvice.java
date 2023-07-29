package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.jointdelivery.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JointDeliveryControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({JointDeliveryNotFoundException.class, StoreNotFoundException.class, MenuNotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    // 403 Forbidden
    @ExceptionHandler({JointDeliveryCartAccessException.class})
    public ResponseEntity<Void> handleForbiddenException(RuntimeException e) {
        return ResponseEntity.status(403).build();
    }

    // 400 Bad Request
    @ExceptionHandler({JointDeliveryExpiredException.class})
    public ResponseEntity<Void> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
