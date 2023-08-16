package com.example.honjarang.domain.secondhandtransaction.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SecondhandTransaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    private Long buyerId;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    private Integer price;

    @ColumnDefault("false")
    private Boolean isComplete;

    @Builder
    public SecondhandTransaction(User user, Long buyerId, String title, String content, Integer price) {
        this.user = user;
        this.buyerId = buyerId;
        this.price = price;
    }

}
