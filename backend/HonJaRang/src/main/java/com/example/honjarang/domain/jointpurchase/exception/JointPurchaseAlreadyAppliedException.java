package com.example.honjarang.domain.jointpurchase.exception;

public class JointPurchaseAlreadyAppliedException extends RuntimeException{
    public JointPurchaseAlreadyAppliedException(String message) {
        super(message);
    }
}
