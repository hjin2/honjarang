package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryApplicant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class JointDeliveryApplicantListDto {
    private Long id;
    private Long userId;
    private String nickname;
    private Integer totalPrice;
    private Boolean isReceived;

    public JointDeliveryApplicantListDto(JointDeliveryApplicant jointDeliveryApplicant, Integer totalPrice) {
        this.id = jointDeliveryApplicant.getId();
        this.userId = jointDeliveryApplicant.getUser().getId();
        this.nickname = jointDeliveryApplicant.getUser().getNickname();
        this.totalPrice = totalPrice;
        this.isReceived = jointDeliveryApplicant.getIsReceived();
    }

    public void addTotalPrice(Integer totalPrice) {
        this.totalPrice += totalPrice;
    }
}
