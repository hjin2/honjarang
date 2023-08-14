package com.example.honjarang.domain.user.exception;

public class VerificationCodeNotFoundException extends RuntimeException{
    public VerificationCodeNotFoundException(String message) {
        super(message);
    }
}
