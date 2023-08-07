package com.example.honjarang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/chat") // WebSocket 연결 URL
                .allowedOrigins("http://localhost:63342", "http://localhost:3000", "https://honjarang.kro.kr", "http://honjarang.kro.kr:3000"); // 허용할 Origin
        registry.addMapping("/api/v1/**") // API 요청 URL
                .allowedOrigins("http://localhost:63342", "http://localhost:3000", "https://honjarang.kro.kr", "http://honjarang.kro.kr:3000"); // 허용할 Origin
    }
}