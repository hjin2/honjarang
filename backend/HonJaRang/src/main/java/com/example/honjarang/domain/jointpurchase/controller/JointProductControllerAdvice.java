package com.example.honjarang.domain.jointpurchase.controller;

import com.example.honjarang.domain.jointpurchase.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JointProductControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({ProductNotFoundException.class, JointPurchaseNotFoundException.class, JointPurchaseApplicantNotFoundException.class, JointPurchaseCanceledException.class})
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    // 403 Forbidden
    @ExceptionHandler({JointPurchaseNotClosedException.class, UnauthorizedJointPurchaseAccessException.class})
    public ResponseEntity<Void> handleForbiddenException() {
        return ResponseEntity.status(403).build();
    }

    // 400 Bad Request
    @ExceptionHandler({JointPurchaseExpiredException.class})
    public ResponseEntity<Void> handleBadRequestException() {
        return ResponseEntity.badRequest().build();
    }

    // 409 Conflict
    @ExceptionHandler({JointPurchaseAlreadyReceivedException.class, JointPurchaseAlreadyAppliedException.class})
    public ResponseEntity<Void> handleConflictException() {
        return ResponseEntity.status(409).build();
    }
}
