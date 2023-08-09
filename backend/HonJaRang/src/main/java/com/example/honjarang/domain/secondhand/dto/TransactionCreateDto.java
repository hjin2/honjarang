package com.example.honjarang.domain.secondhand.dto;

import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionCreateDto {
    private String title;
    private String content;
    private Integer price;

    public Transaction toEntity(User user, String transactionImage) {
        return Transaction.builder()
                .content(this.content)
                .title(this.title)
                .seller(user)
                .price(this.price)
                .transactionImage(transactionImage)
                .build();
    }
}
