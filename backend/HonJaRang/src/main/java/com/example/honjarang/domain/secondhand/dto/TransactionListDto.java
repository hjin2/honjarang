package com.example.honjarang.domain.secondhand.dto;

import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransactionListDto {
    private Long id;
    private String title;
    private Boolean isComplete;
    private Integer price;
    private String transactionImage;


    public TransactionListDto(Transaction transaction){
        this.id = transaction.getId();
        this.title = transaction.getTitle();
        this.isComplete = transaction.getIsCompleted();
        this.price = transaction.getPrice();
        this.transactionImage = transaction.getTransactionImage();
    }
}
