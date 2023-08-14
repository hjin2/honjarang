package com.example.honjarang.domain.user.exception;

public class EmailAlreadyVerifiedException extends RuntimeException{
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}
