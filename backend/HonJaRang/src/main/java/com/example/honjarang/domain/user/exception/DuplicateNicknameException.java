package com.example.honjarang.domain.user.exception;

public class DuplicateNicknameException extends RuntimeException{
    public DuplicateNicknameException(String message) {
        super(message);
    }
}
