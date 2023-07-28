package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.user.dto.LoginDto;
import com.example.honjarang.domain.user.dto.PasswordUpdateDto;
import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.dto.UserInfoUpdateDto;
import com.example.honjarang.domain.user.dto.VerifyCodeDto;
import com.example.honjarang.domain.user.entity.User;

import com.example.honjarang.domain.user.service.EmailService;
import com.example.honjarang.domain.user.service.S3Uploader;
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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final TokenService tokenService;

    private final EmailService emailService;

    private final S3Uploader s3Uploader;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        User user = userService.login(loginDto);
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
        emailService.verifyCode(verifyCodeDto.getEmail(), verifyCodeDto.getCode());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Void> checkNickname(@RequestParam String nickname) {
        userService.checkNickname(nickname);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserCreateDto userCreateDto) {
        userService.signup(userCreateDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody PasswordUpdateDto passwordUpdateDto, @CurrentUser User user) {
        userService.changePassword(user, passwordUpdateDto.getPassword(), passwordUpdateDto.getNewPassword());
    }

    @PutMapping("/users")
    public ResponseEntity<Void> changeUserInfo(@RequestBody UserInfoUpdateDto userInfoUpdateDto, @CurrentUser User user) {
        userService.changeUserInfo(user, userInfoUpdateDto.getNickname(), userInfoUpdateDto.getAddress(), userInfoUpdateDto.getLatitude(), userInfoUpdateDto.getLongitude());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-image")
    public ResponseEntity<Void> upload(@RequestParam String profileImage, @CurrentUser User user){
        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            if(!user.getProfileImage().equals(profileImage)) {
                s3Uploader.delete(user.getProfileImage());
            }
        }
        userService.changeUserImage(user, profileImage);
        return ResponseEntity.ok().build();
    }

}
