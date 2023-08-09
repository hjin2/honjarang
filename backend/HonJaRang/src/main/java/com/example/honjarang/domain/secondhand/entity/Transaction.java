package com.example.honjarang.domain.secondhand.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.secondhand.dto.TransactionUpdateDto;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;



@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
public class Transaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @JoinColumn(name = "seller_id")
    @ManyToOne
    private User seller;

    @JoinColumn(name = "buyer_id")
    @ManyToOne
    private User buyer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer price;

    @Column
    @ColumnDefault("false")
    private Boolean isCompleted;

    @Column
    @ColumnDefault("false")
    private Boolean isReceived;

    @Column
    private String transactionImage;

    @Builder
    public Transaction(String title, String content, Integer price, User seller, User buyer, Boolean isCompleted, Boolean isReceived, String transactionImage) {
        this.seller = seller;
        this.buyer = buyer;
        this.title = title;
        this.content = content;
        this.price = price;
        this.isCompleted = isCompleted;
        this.isReceived = isReceived;
        this.transactionImage = transactionImage;
    }

    public void complete() {
        this.isCompleted = true;
    }

    public void receive(){this.isReceived = true;}

    public void soldout(User user){this.buyer = user;}

    public void update(TransactionUpdateDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.price = dto.getPrice();
    }

    public void setIdForTest(Long id) {this.Id = id;}

    public void setPriceForTest(Integer price){this.price = price;}


}
