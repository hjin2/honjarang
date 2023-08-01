package com.example.honjarang.domain.jointpurchase.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointPurchaseCreateDto {
    private String content;
    private String deadline;
    private Integer targetPersonCount;
    private String productName;
    private Integer price;
    private Integer deliveryCharge;
    private String placeKeyword;

    public JointPurchase toEntity(JointPurchaseCreateDto jointPurchaseCreateDto, User user, String image, PlaceDto placeDto) {
        return JointPurchase.builder()
                .content(jointPurchaseCreateDto.getContent())
                .deadline(DateTimeUtils.parseLocalDateTime(jointPurchaseCreateDto.getDeadline()))
                .targetPersonCount(jointPurchaseCreateDto.getTargetPersonCount())
                .productName(jointPurchaseCreateDto.getProductName())
                .image(image)
                .price(jointPurchaseCreateDto.getPrice())
                .deliveryCharge(jointPurchaseCreateDto.getDeliveryCharge())
                .placeName(placeDto.getPlaceName())
                .latitude(placeDto.getLatitude())
                .longitude(placeDto.getLongitude())
                .user(user)
                .build();
    }
}
