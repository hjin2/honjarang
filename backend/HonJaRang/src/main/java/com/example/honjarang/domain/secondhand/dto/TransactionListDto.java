package com.example.honjarang.domain.secondhand.dto;

import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransactionListDto {
    private String title;
    private Boolean isComplete;
    private Integer price;


    public TransactionListDto(Transaction transaction){
        this.title = transaction.getTitle();
        this.isComplete = transaction.getIsCompleted();
        this.price = transaction.getPrice();
    }
}
