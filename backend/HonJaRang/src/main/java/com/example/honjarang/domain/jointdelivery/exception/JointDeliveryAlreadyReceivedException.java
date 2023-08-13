package com.example.honjarang.domain.jointdelivery.exception;

public class JointDeliveryAlreadyReceivedException extends RuntimeException{
    public JointDeliveryAlreadyReceivedException(String message) {
        super(message);
    }
}
