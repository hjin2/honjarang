package com.example.honjarang.domain.jointpurchase.exception;

public class InsufficientPersonCountException extends RuntimeException {
    public InsufficientPersonCountException(String message) {
        super(message);
    }
}
