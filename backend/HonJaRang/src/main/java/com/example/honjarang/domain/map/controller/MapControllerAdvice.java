package com.example.honjarang.domain.map.controller;

import com.example.honjarang.domain.map.exception.LocationNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MapControllerAdvice {
    // 404 Not Found
    @ExceptionHandler({LocationNotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}
