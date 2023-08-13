package com.example.honjarang.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TimeController {
    @GetMapping("/time")
    public String time() {
        LocalDateTime now = LocalDateTime.now();
        return now.toString();
    }
}
