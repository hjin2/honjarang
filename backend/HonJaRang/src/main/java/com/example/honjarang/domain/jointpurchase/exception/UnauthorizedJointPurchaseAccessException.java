package com.example.honjarang.domain.jointpurchase.exception;

public class UnauthorizedJointPurchaseAccessException extends RuntimeException{
    public UnauthorizedJointPurchaseAccessException(String message) {
        super(message);
    }
}
