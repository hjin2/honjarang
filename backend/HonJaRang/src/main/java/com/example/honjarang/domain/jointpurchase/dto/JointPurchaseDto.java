package com.example.honjarang.domain.jointpurchase.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointPurchaseDto {
    private Long id;
    private String productName;
    private String image;
    private String content;
    private Integer price;
    private Integer currentPersonCount;
    private Integer targetPersonCount;
    private Integer deliveryCharge;
    private String deadline;
    private String placeName;
    private Double placeLatitude;
    private Double placeLongitude;
    private String createdAt;
    private Long userId;
    private String nickname;

    public JointPurchaseDto(JointPurchase jointPurchase, Integer currentPersonCount) {
        this.id = jointPurchase.getId();
        this.productName = jointPurchase.getProductName();
        this.image = jointPurchase.getImage();
        this.content = jointPurchase.getContent();
        this.price = jointPurchase.getPrice();
        this.currentPersonCount = currentPersonCount;
        this.targetPersonCount = jointPurchase.getTargetPersonCount();
        this.deliveryCharge = jointPurchase.getDeliveryCharge();
        this.deadline = DateTimeUtils.formatLocalDateTime(jointPurchase.getDeadline());
        this.placeName = jointPurchase.getPlaceName();
        this.placeLatitude = jointPurchase.getLatitude();
        this.placeLongitude = jointPurchase.getLongitude();
        this.createdAt = DateTimeUtils.formatLocalDateTime(jointPurchase.getCreatedAt());
        this.userId = jointPurchase.getUser().getId();
        this.nickname = jointPurchase.getUser().getNickname();
    }
}
