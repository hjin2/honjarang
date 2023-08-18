package com.example.honjarang.domain.jointpurchase.controller;

import com.example.honjarang.domain.jointpurchase.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class JointProductControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({ProductNotFoundException.class, JointPurchaseNotFoundException.class, JointPurchaseApplicantNotFoundException.class, JointPurchaseCanceledException.class})
    public ResponseEntity<Void> handleNotFoundException(RuntimeException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    // 403 Forbidden
    @ExceptionHandler({JointPurchaseNotClosedException.class, UnauthorizedJointPurchaseAccessException.class})
    public ResponseEntity<Void> handleForbiddenException(RuntimeException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(403).build();
    }

    // 400 Bad Request
    @ExceptionHandler({JointPurchaseExpiredException.class, InsufficientPersonCountException.class})
    public ResponseEntity<Void> handleBadRequestException(RuntimeException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    // 409 Conflict
    @ExceptionHandler({JointPurchaseAlreadyReceivedException.class, JointPurchaseAlreadyAppliedException.class})
    public ResponseEntity<Void> handleConflictException(RuntimeException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(409).build();
    }
}
