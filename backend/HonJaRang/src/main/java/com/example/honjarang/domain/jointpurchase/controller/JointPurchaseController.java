package com.example.honjarang.domain.jointpurchase.controller;

import com.example.honjarang.domain.jointpurchase.dto.JointPurchaseCreateDto;
import com.example.honjarang.domain.jointpurchase.service.JointPurchaseService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/joint-purchases")
public class JointPurchaseController {
    private final JointPurchaseService jointPurchaseService;

    @PostMapping("")
    public ResponseEntity<Void> createJointPurchase(@RequestBody JointPurchaseCreateDto jointPurchaseCreateDto, @CurrentUser User user) {
        jointPurchaseService.createJointPurchase(jointPurchaseCreateDto, user);
        return ResponseEntity.ok().build();
    }
}
