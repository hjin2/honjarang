package com.example.honjarang.domain.secondhand.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransactionDto {

    private Long id;
    private User seller;
    private User buyer;
    private String title;
    private String content;
    private Integer price;
    private Boolean isCompleted;
    private String createdAt;
    private String updatedat;



    public TransactionDto(Transaction transaction){
        this.seller = transaction.getSeller();
        this.title = transaction.getTitle();
        this.content = transaction.getContent();
        this.price = transaction.getPrice();
        this.isCompleted = transaction.getIsCompleted();
        this.createdAt = DateTimeUtils.formatLocalDateTime(transaction.getCreatedAt());
    }


}
