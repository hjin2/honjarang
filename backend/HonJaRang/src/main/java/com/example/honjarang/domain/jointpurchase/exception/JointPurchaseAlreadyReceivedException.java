package com.example.honjarang.domain.jointpurchase.exception;

public class JointPurchaseAlreadyReceivedException extends RuntimeException{
    public JointPurchaseAlreadyReceivedException(String message) {
        super(message);
    }
}
