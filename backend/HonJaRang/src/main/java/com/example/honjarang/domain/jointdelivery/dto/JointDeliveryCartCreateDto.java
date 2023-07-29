package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointDeliveryCartCreateDto {
    private Long jointDeliveryId;
    private String menuId;
    private Integer quantity;

    public JointDeliveryCart toEntity(JointDeliveryCartCreateDto dto, User user) {
        return JointDeliveryCart.builder()
                .menuId(dto.getMenuId())
                .quantity(dto.getQuantity())
                .user(user)
                .build();
    }
}
