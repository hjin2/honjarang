package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.jointdelivery.dto.*;
import com.example.honjarang.domain.jointdelivery.service.JointDeliveryService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/joint-deliveries")
public class JointDeliveryController {
    private final JointDeliveryService jointDeliveryService;

    @GetMapping("/stores")
    public ResponseEntity<List<StoreListDto>> getStoreList(String keyword) {
        List<StoreListDto> storeListDtoList = jointDeliveryService.getStoreListByApi(keyword);
//        List<StoreListDto> storeListDtoList = jointDeliveryService.getStoreListByApiForTest(keyword);
        return ResponseEntity.ok(storeListDtoList);
    }

    @GetMapping("{jointDeliveryId}/menus")
    public ResponseEntity<List<MenuListDto>> getMenuList(@PathVariable Long jointDeliveryId) {
        List<MenuListDto> menuListDtoList = jointDeliveryService.getMenuList(jointDeliveryId);
        return ResponseEntity.ok(menuListDtoList);
    }

    @PostMapping("")
    public ResponseEntity<Long> createJointDelivery(@RequestBody JointDeliveryCreateDto dto, @CurrentUser User user) {
        return ResponseEntity.ok(jointDeliveryService.createJointDelivery(dto, user));
    }

    @DeleteMapping("/{jointDeliveryId}")
    public ResponseEntity<Void> cancelJointDelivery(@PathVariable Long jointDeliveryId, @CurrentUser User user) {
        jointDeliveryService.cancelJointDelivery(jointDeliveryId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<JointDeliveryListDto>> getJointDeliveryList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer size, @RequestParam(defaultValue = "") String keyword, @CurrentUser User user) {
        List<JointDeliveryListDto> jointDeliveryList = jointDeliveryService.getJointDeliveryList(page, size, keyword, user);
        return ResponseEntity.ok(jointDeliveryList);
    }

    @GetMapping("/{jointDeliveryId}")
    public ResponseEntity<JointDeliveryDto> getJointDelivery(@PathVariable Long jointDeliveryId, @CurrentUser User user) {
        JointDeliveryDto jointDelivery = jointDeliveryService.getJointDelivery(jointDeliveryId, user);
        return ResponseEntity.ok(jointDelivery);
    }

    @GetMapping("/{jointDeliveryId}/carts")
    public ResponseEntity<List<JointDeliveryCartListDto>> getJointDeliveryCartList(@PathVariable Long jointDeliveryId, @CurrentUser User user) {
        List<JointDeliveryCartListDto> jointDeliveryCartList = jointDeliveryService.getJointDeliveryCartList(jointDeliveryId, user);
        return ResponseEntity.ok(jointDeliveryCartList);
    }

    @PostMapping("/{jointDeliveryId}/carts")
    public ResponseEntity<Void> addJointDeliveryCart(@PathVariable Long jointDeliveryId, @RequestBody JointDeliveryCartCreateDto dto, @CurrentUser User user) {
        jointDeliveryService.addJointDeliveryCart(dto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jointDeliveryId}/carts/{jointDeliveryCartId}")
    public ResponseEntity<Void> removeJointDeliveryCart(@PathVariable Long jointDeliveryId, @PathVariable Long jointDeliveryCartId, @CurrentUser User user) {
        jointDeliveryService.removeJointDeliveryCart(jointDeliveryCartId, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{jointDeliveryId}/receive")
    public ResponseEntity<Void> confirmReceipt(@PathVariable Long jointDeliveryId, @CurrentUser User user) {
        jointDeliveryService.confirmReceived(jointDeliveryId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Integer> getJointDeliveryPage(@RequestParam Integer size) {
        return ResponseEntity.ok(jointDeliveryService.getJointDeliveryPageCount(size));
    }
}
