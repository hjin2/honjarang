package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.service.EmailService;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final TokenService tokenService;

    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(String email, String password) {
        User user = userService.login(email, password);
        TokenDto tokenDto = tokenService.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<Void> sendVerificationCode(String email) {
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(String email, String code) {
        Boolean isVerified = emailService.verifyCode(email, code);
        return ResponseEntity.ok(isVerified);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(String nickname) {
        Boolean isAvailable = userService.checkNickname(nickname);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(UserCreateDto userCreateDto) {
        userService.signup(userCreateDto);
        return ResponseEntity.ok().build();
    }
}
