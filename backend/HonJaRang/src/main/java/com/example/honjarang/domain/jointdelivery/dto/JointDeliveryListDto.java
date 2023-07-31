package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointDeliveryListDto {
    private Long id;
    private Integer currentTotalPrice;
    private Integer targetMinPrice;

    private Long storeId;
    private String storeName;
    private String storeImage;

    private Long userId;
    private String nickname;

    public JointDeliveryListDto(JointDelivery jointDelivery, Integer currentTotalPrice) {
        this.id = jointDelivery.getId();
        this.currentTotalPrice = currentTotalPrice;
        this.targetMinPrice = jointDelivery.getTargetMinPrice();
        this.storeId = jointDelivery.getStore().getId();
        this.storeName = jointDelivery.getStore().getStoreName();
        this.storeImage = jointDelivery.getStore().getImage();
        this.userId = jointDelivery.getUser().getId();
        this.nickname = jointDelivery.getUser().getNickname();
    }
}
