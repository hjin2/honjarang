package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.jointdelivery.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JointDeliveryControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({JointDeliveryNotFoundException.class, StoreNotFoundException.class, MenuNotFoundException.class, JointDeliveryApplicantNotFoundException.class, JointDeliveryCanceledException.class})
    public ResponseEntity<Void> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    // 403 Forbidden
    @ExceptionHandler({JointDeliveryCartAccessException.class, JointDeliveryNotClosedException.class, UnauthorizedJointDeliveryAccessException.class})
    public ResponseEntity<Void> handleForbiddenException(RuntimeException e) {
        return ResponseEntity.status(403).build();
    }

    // 400 Bad Request
    @ExceptionHandler({JointDeliveryExpiredException.class})
    public ResponseEntity<Void> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }

    // 409 Conflict
    @ExceptionHandler({JointDeliveryAlreadyReceivedException.class})
    public ResponseEntity<Void> handleConflictException(RuntimeException e) {
        return ResponseEntity.status(409).build();
    }
}
