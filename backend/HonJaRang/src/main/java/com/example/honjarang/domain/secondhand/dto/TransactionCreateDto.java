package com.example.honjarang.domain.secondhand.dto;

import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionCreateDto {
    private String title;
    private String content;
    private Integer price;

    public Transaction toEntity(TransactionCreateDto dto, User user) {
        return Transaction.builder()
                .content(dto.getContent())
                .title(dto.getTitle())
                .seller(user)
                .price(dto.getPrice())
                .build();
    }
}
