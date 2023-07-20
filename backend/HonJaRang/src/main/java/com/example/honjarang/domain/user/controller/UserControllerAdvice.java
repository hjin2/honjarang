package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.exception.PasswordMismatchException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Void> handlePasswordMismatchException(PasswordMismatchException e) {
        return ResponseEntity.badRequest().build();
    }
}
