package com.example.honjarang.domain.user.exception;

public class VerificationCodeExpiredException extends RuntimeException{
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}
