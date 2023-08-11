package com.example.honjarang.domain.jointpurchase.controller;

import com.example.honjarang.domain.jointpurchase.dto.*;
import com.example.honjarang.domain.jointpurchase.service.JointPurchaseService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/joint-purchases")
public class JointPurchaseController {
    private final JointPurchaseService jointPurchaseService;

    @PostMapping("")
    public ResponseEntity<Long> createJointPurchase(@RequestBody JointPurchaseCreateDto jointPurchaseCreateDto, @CurrentUser User user) {
        return ResponseEntity.ok(jointPurchaseService.createJointPurchase(jointPurchaseCreateDto, user));
    }

    @DeleteMapping("/{jointPurchaseId}")
    public ResponseEntity<Void> cancelJointPurchase(@PathVariable Long jointPurchaseId, @CurrentUser User user) {
        jointPurchaseService.cancelJointPurchase(jointPurchaseId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<JointPurchaseListDto>> getJointPurchases(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer size, @RequestParam(defaultValue = "") String keyword, @CurrentUser User user) {
        List<JointPurchaseListDto> jointPurchaseListDtos = jointPurchaseService.getJointPurchaseList(page, size, keyword, user);
        return ResponseEntity.ok(jointPurchaseListDtos);
    }

    @GetMapping("/{jointPurchaseId}")
    public ResponseEntity<JointPurchaseDto> getJointPurchase(@PathVariable Long jointPurchaseId, @CurrentUser User user) {
        JointPurchaseDto jointPurchaseDto = jointPurchaseService.getJointPurchase(jointPurchaseId, user);
        return ResponseEntity.ok(jointPurchaseDto);
    }

    @GetMapping("/{jointPurchaseId}/applicants")
    public ResponseEntity<List<JointPurchaseApplicantListDto>> getJointPurchaseApplicants(@PathVariable Long jointPurchaseId) {
        List<JointPurchaseApplicantListDto> jointPurchaseApplicantListDtos = jointPurchaseService.getJointPurchaseApplicantList(jointPurchaseId);
        return ResponseEntity.ok(jointPurchaseApplicantListDtos);
    }

    @PostMapping("/{jointPurchaseId}/applicants")
    public ResponseEntity<Void> applyJointPurchase(@PathVariable Long jointPurchaseId, @RequestBody JointPurchaseApplyDto jointPurchaseApplyDto, @CurrentUser User user) {
        jointPurchaseService.applyJointPurchase(jointPurchaseApplyDto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jointPurchaseId}/applicants")
    public ResponseEntity<Void> cancelJointPurchaseApplicant(@PathVariable Long jointPurchaseId, @CurrentUser User user) {
        jointPurchaseService.cancelJointPurchaseApplicant(jointPurchaseId, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{jointPurchaseId}/receive")
    public ResponseEntity<Void> confirmReceived(@PathVariable Long jointPurchaseId, @CurrentUser User user) {
        log.info("수령 확인 요청");
        jointPurchaseService.confirmReceived(jointPurchaseId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Integer> getJointPurchasePage(@RequestParam Integer size) {
        Integer jointPurchasePage = jointPurchaseService.getJointPurchasePageCount(size);
        return ResponseEntity.ok(jointPurchasePage);
    }
}
