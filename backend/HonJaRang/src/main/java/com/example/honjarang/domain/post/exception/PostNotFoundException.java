package com.example.honjarang.domain.post.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) { super(message); }
}
