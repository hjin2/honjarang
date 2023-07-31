package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointDeliveryCreateDto {
    private String content;
    private Long storeId;
    private Integer deliveryCharge;
    private Integer targetMinPrice;
    private String deadline;

    public JointDelivery toEntity(JointDeliveryCreateDto dto, Store store, User user) {
        return JointDelivery.builder()
                .content(dto.getContent())
                .deliveryCharge(dto.getDeliveryCharge())
                .targetMinPrice(dto.getTargetMinPrice())
                .deadline(DateTimeUtils.parseLocalDateTime(dto.getDeadline()))
                .store(store)
                .user(user)
                .build();
    }
}
