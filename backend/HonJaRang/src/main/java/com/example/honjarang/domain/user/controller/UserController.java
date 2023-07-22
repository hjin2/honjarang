package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/change-password")
    public void changePassword(String existPassword, String newPassword){
        User logineduser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changePassword(logineduser,existPassword, newPassword);
    }
}
