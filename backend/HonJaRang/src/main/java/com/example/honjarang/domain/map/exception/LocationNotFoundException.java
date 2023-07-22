package com.example.honjarang.domain.map.exception;

public class LocationNotFoundException extends RuntimeException{
    public LocationNotFoundException(String message) {
        super(message);
    }
}
