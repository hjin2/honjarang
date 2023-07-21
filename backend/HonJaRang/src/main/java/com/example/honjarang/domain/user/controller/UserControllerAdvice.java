package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    // 404 Not Found
    @ExceptionHandler({UserNotFoundException.class, VerificationCodeNotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    // 400 Bad Request
    @ExceptionHandler({PasswordMismatchException.class, VerificationCodeMismatchException.class, VerificationCodeExpiredException.class})
    public ResponseEntity<Void> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }

    // 409 Conflict
    @ExceptionHandler({EmailAlreadyVerifiedException.class})
    public ResponseEntity<Void> handleConflictException(RuntimeException e) {
        return ResponseEntity.status(409).build();
    }
}
