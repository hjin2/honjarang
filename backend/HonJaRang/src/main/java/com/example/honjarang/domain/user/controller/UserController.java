package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final TokenService tokenService;
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(String email, String password) {
        User user = userService.login(email, password);
        TokenDto tokenDto = tokenService.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(tokenDto);
    }
}
