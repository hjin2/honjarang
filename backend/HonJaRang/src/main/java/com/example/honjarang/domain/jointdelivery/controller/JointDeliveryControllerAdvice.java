package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.jointdelivery.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class JointDeliveryControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({JointDeliveryNotFoundException.class, JointDeliveryCartNotFoundException.class, StoreNotFoundException.class, MenuNotFoundException.class, JointDeliveryApplicantNotFoundException.class, JointDeliveryCanceledException.class})
    public ResponseEntity<Void> handleNotFoundException(RuntimeException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    // 403 Forbidden
    @ExceptionHandler({JointDeliveryCartAccessException.class, JointDeliveryNotClosedException.class, UnauthorizedJointDeliveryAccessException.class})
    public ResponseEntity<Void> handleForbiddenException(RuntimeException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(403).build();
    }

    // 400 Bad Request
    @ExceptionHandler({JointDeliveryExpiredException.class})
    public ResponseEntity<Void> handleBadRequestException(RuntimeException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    // 409 Conflict
    @ExceptionHandler({JointDeliveryAlreadyReceivedException.class})
    public ResponseEntity<Void> handleConflictException(RuntimeException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(409).build();
    }
}
