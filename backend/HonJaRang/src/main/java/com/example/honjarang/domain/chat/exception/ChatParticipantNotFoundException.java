package com.example.honjarang.domain.chat.exception;

public class ChatParticipantNotFoundException extends RuntimeException{
    public ChatParticipantNotFoundException(String message) {
        super(message);
    }
}
