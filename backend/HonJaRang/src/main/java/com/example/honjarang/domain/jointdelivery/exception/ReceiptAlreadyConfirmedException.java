package com.example.honjarang.domain.jointdelivery.exception;

public class ReceiptAlreadyConfirmedException extends RuntimeException{
    public ReceiptAlreadyConfirmedException(String message) {
        super(message);
    }
}
