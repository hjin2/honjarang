package com.example.honjarang.domain.map.exception;

public class PlaceNotFoundException extends RuntimeException{
    public PlaceNotFoundException(String message) {
        super(message);
    }
}
