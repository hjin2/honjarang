package com.example.honjarang.domain.secondhand.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransactionDto {

    private Long id;
    private Long sellerId;
    private String sellerNickname;
    private String title;
    private String content;
    private Integer price;
    private Boolean isCompleted;
    private String createdAt;
    private String transactionImage;
    private Boolean isReceived;
    private Long buyerId;



    public TransactionDto(Transaction transaction, Long buyerId){
        this.id = transaction.getId();
        this.sellerId = transaction.getSeller().getId();
        this.sellerNickname = transaction.getSeller().getNickname();
        this.title = transaction.getTitle();
        this.content = transaction.getContent();
        this.price = transaction.getPrice();
        this.isCompleted = transaction.getIsCompleted();
        this.createdAt = DateTimeUtils.formatLocalDateTime(transaction.getCreatedAt());
        this.isReceived = transaction.getIsReceived();

        if(transaction.getTransactionImage().equals("")){
            this.transactionImage = "";
        }else{
            this.transactionImage = "https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/transactionImage/" + transaction.getTransactionImage();
        }
        this.buyerId = buyerId;
    }


}
