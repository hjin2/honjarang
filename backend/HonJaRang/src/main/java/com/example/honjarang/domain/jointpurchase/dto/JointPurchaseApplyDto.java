package com.example.honjarang.domain.jointpurchase.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointPurchaseApplyDto {
    private Long jointPurchaseId;
    private Integer quantity;
}
