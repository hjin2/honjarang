package com.example.honjarang.domain.user.exception;

public class InsufficientPointsException extends RuntimeException{
    public InsufficientPointsException(String message) {
        super(message);
    }
}
