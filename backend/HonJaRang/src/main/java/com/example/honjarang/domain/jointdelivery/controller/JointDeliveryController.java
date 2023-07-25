package com.example.honjarang.domain.jointdelivery.controller;

import com.example.honjarang.domain.jointdelivery.dto.CreateJoinDeliveryDto;
import com.example.honjarang.domain.jointdelivery.dto.MenuListDto;
import com.example.honjarang.domain.jointdelivery.dto.StoreListDto;
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
        return ResponseEntity.ok(storeListDtoList);
    }

    @GetMapping("{deliveryId}/menus")
    public ResponseEntity<List<MenuListDto>> getMenuList(@PathVariable Long deliveryId) {
        List<MenuListDto> menuListDtoList = jointDeliveryService.getMenuList(deliveryId);
        return ResponseEntity.ok(menuListDtoList);
    }

    @PostMapping("")
    public ResponseEntity<Void> createJointDelivery(@RequestBody CreateJoinDeliveryDto dto, @CurrentUser User user) {
        jointDeliveryService.createJointDelivery(dto, user);
        return ResponseEntity.ok().build();
    }
}
