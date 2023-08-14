package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.chat.entity.ChatRoom;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JointDeliveryCreateDto {
    private String content;
    private Long storeId;
    private Integer deliveryCharge;
    private Integer targetMinPrice;
    private String deadline;

    public JointDelivery toEntity(Store store, User user, ChatRoom chatRoom) {
        return JointDelivery.builder()
                .content(content)
                .deliveryCharge(deliveryCharge)
                .targetMinPrice(targetMinPrice)
                .deadline(DateTimeUtils.parseLocalDateTime(deadline))
                .store(store)
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }
}
