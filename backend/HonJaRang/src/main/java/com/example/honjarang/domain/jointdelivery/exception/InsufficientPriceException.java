package com.example.honjarang.domain.jointdelivery.exception;

public class InsufficientPriceException extends RuntimeException{
    public InsufficientPriceException(String message) {
        super(message);
    }
}
