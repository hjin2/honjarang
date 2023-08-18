package com.example.honjarang.domain.jointpurchase.dto;

import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointPurchaseListDto {
    private Long id;
    private String productName;
    private String image;
    private Integer price;
    private Integer currentPersonCount;
    private Integer targetPersonCount;

    public JointPurchaseListDto(JointPurchase jointPurchase, Integer currentPersonCount) {
        this.id = jointPurchase.getId();
        this.productName = jointPurchase.getProductName();
        this.image = jointPurchase.getImage();
        this.price = jointPurchase.getPrice();
        this.currentPersonCount = currentPersonCount;
        this.targetPersonCount = jointPurchase.getTargetPersonCount();
    }

    public JointPurchaseListDto(JointPurchase jointPurchase){
        this.id = jointPurchase.getId();
        this.productName = jointPurchase.getProductName();
        this.image = jointPurchase.getImage();
        this.price = jointPurchase.getPrice();
        this.targetPersonCount = jointPurchase.getTargetPersonCount();
    }
}
