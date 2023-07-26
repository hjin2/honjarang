package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointDeliveryCreateDto {
    private String content;
    private Long storeId;
    private Integer deliveryCharge;
    private Integer targetMinPrice;
    private LocalDateTime deadline;

    public JointDelivery toEntity(JointDeliveryCreateDto dto, Store store, User user) {
        return JointDelivery.builder()
                .content(dto.getContent())
                .deliveryCharge(dto.getDeliveryCharge())
                .targetMinPrice(dto.getTargetMinPrice())
                .deadline(dto.getDeadline())
                .store(store)
                .user(user)
                .build();
    }
}
