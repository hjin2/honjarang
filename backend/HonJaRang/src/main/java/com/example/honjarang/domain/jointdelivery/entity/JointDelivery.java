package com.example.honjarang.domain.jointdelivery.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.jointdelivery.dto.CreateJoinDeliveryDto;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class JointDelivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "store_id")
    @ManyToOne
    private Store store;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer deliveryCharge;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column
    @ColumnDefault("false")
    private Boolean isCanceled;

    @Builder
    public JointDelivery(String content, Integer deliveryCharge, LocalDateTime deadline, Store store, User user) {
        this.content = content;
        this.deliveryCharge = deliveryCharge;
        this.deadline = deadline;
        this.store = store;
        this.user = user;
    }

    public void cancel() {
        this.isCanceled = true;
    }
}
