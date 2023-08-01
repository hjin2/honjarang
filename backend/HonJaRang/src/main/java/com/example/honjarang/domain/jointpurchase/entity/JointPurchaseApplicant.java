package com.example.honjarang.domain.jointpurchase.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@NoArgsConstructor
@Entity
public class JointPurchaseApplicant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "joint_purchase_id")
    @ManyToOne
    private JointPurchase jointPurchase;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Integer quantity;

    @Column
    @ColumnDefault("false")
    private Boolean isReceived;

    @Builder
    public JointPurchaseApplicant(JointPurchase jointPurchase, User user, Integer quantity, Boolean isReceived) {
        this.jointPurchase = jointPurchase;
        this.user = user;
        this.quantity = quantity;
        this.isReceived = isReceived;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }
}
