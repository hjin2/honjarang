package com.example.honjarang.domain.jointpurchase.dto;

import com.example.honjarang.domain.jointpurchase.entity.JointPurchaseApplicant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointPurchaseApplicantListDto {
    private Long id;
    private Long userId;
    private String nickname;
    private Integer quantity;
    private Integer totalPrice;
    private Boolean isReceived;

    public JointPurchaseApplicantListDto(JointPurchaseApplicant jointPurchaseApplicant) {
        this.id = jointPurchaseApplicant.getId();
        this.userId = jointPurchaseApplicant.getUser().getId();
        this.nickname = jointPurchaseApplicant.getUser().getNickname();
        this.quantity = jointPurchaseApplicant.getQuantity();
        this.totalPrice = jointPurchaseApplicant.getQuantity() * jointPurchaseApplicant.getJointPurchase().getPrice();
        this.isReceived = jointPurchaseApplicant.getIsReceived();
    }
}
