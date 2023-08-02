package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    public JointDeliveryListDto(JointDelivery jointDelivery) {
        this.id = jointDelivery.getId();
        this.targetMinPrice = jointDelivery.getTargetMinPrice();
        this.storeId = jointDelivery.getStore().getId();
        this.storeName = jointDelivery.getStore().getStoreName();
        this.storeImage = jointDelivery.getStore().getImage();
        this.userId = jointDelivery.getUser().getId();
        this.nickname = jointDelivery.getUser().getNickname();
    }

    public JointDeliveryListDto(Long id, Integer targetMinPrice, Long storeId, String storeName, String storeImage, Long userId, String nickname){
        this.id = id;
        this.targetMinPrice = targetMinPrice;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeImage = storeImage;
        this.userId = userId;
        this.nickname = nickname;
    }
}
