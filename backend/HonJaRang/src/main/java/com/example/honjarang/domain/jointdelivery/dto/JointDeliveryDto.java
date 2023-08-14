package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointDeliveryDto {
    private Long id;
    private String content;
    private Integer deliveryCharge;
    private Integer currentTotalPrice;
    private Integer targetMinPrice;
    private String deadline;
    private String createdAt;

    private Long storeId;
    private String storeName;
    private String storeImage;

    private Long userId;
    private String nickname;
    private Integer myPoint;

    private Long chatRoomId;

    public JointDeliveryDto(JointDelivery jointDelivery, Integer currentTotalPrice, Integer myPoint) {
        this.id = jointDelivery.getId();
        this.content = jointDelivery.getContent();
        this.deliveryCharge = jointDelivery.getDeliveryCharge();
        this.currentTotalPrice = currentTotalPrice;
        this.targetMinPrice = jointDelivery.getTargetMinPrice();
        this.deadline = DateTimeUtils.formatLocalDateTime(jointDelivery.getDeadline());
        this.createdAt = DateTimeUtils.formatLocalDateTime(jointDelivery.getCreatedAt());
        this.storeId = jointDelivery.getStore().getId();
        this.storeName = jointDelivery.getStore().getStoreName();
        this.storeImage = jointDelivery.getStore().getImage();
        this.userId = jointDelivery.getUser().getId();
        this.nickname = jointDelivery.getUser().getNickname();
        this.myPoint = myPoint;
        this.chatRoomId = jointDelivery.getChatRoom().getId();
    }
}
