package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.dto.LoginDto;
import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.dto.VerifyCodeDto;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.service.EmailService;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.CurrentUser;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final TokenService tokenService;

    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        User user = userService.login(loginDto.getEmail(), loginDto.getPassword());
        TokenDto tokenDto = tokenService.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody Map<String, String> body) {
        emailService.sendVerificationCode(body.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyCodeDto verifyCodeDto) {
        Boolean isVerified = emailService.verifyCode(verifyCodeDto.getEmail(), verifyCodeDto.getCode());
        return ResponseEntity.ok(isVerified);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        Boolean isAvailable = userService.checkNickname(nickname);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserCreateDto userCreateDto) {
        userService.signup(userCreateDto);
        return ResponseEntity.ok().build();
    }
}
