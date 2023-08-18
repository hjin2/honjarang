package com.example.honjarang.domain.secondhandtransaction.dto;

import com.example.honjarang.domain.secondhandtransaction.entity.SecondhandTransaction;
import com.example.honjarang.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class SecondhandTranscationCreateDto {
    private Long id;
    private Long userId;
    private Long buyerId;
    private String title;
    private String content;
    private Integer price;
    private String createdAt;
    private String updatedAt;
    private Boolean isComplete;

    public SecondhandTransaction toEntity(User user) {
        return SecondhandTransaction.builder()
                .user(user)
                .title(title)
                .content(content)
                .price(price)
                .build();
    }
}
