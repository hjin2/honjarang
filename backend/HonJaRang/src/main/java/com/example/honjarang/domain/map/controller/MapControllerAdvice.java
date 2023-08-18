package com.example.honjarang.domain.map.controller;

import com.example.honjarang.domain.map.exception.PlaceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MapControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({PlaceNotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}
