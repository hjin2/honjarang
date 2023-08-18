package com.example.honjarang.domain.user.exception;

public class VerificationCodeMismatchException extends RuntimeException{
    public VerificationCodeMismatchException(String message) {
        super(message);
    }
}
