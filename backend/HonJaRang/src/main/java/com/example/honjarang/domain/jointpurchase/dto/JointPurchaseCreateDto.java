package com.example.honjarang.domain.jointpurchase.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.chat.entity.ChatRoom;
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
    private Double latitude;
    private Double longitude;

    public JointPurchase toEntity(User user, String image, ChatRoom chatRoom) {
        return JointPurchase.builder()
                .content(content)
                .deadline(DateTimeUtils.parseLocalDateTime(deadline))
                .targetPersonCount(targetPersonCount)
                .productName(productName)
                .image(image)
                .price(price)
                .deliveryCharge(deliveryCharge)
                .placeName(placeKeyword)
                .latitude(latitude)
                .longitude(longitude)
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }
}
