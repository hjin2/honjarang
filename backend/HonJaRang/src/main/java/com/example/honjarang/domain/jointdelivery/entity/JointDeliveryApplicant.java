package com.example.honjarang.domain.jointdelivery.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
public class JointDeliveryApplicant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "joint_delivery_id")
    @ManyToOne
    private JointDelivery jointDelivery;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    // 수령여부
    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isReceived;

    @Builder
    public JointDeliveryApplicant(JointDelivery jointDelivery, User user, Boolean isReceived) {
        this.jointDelivery = jointDelivery;
        this.user = user;
        this.isReceived = isReceived;
    }

    public void confirmReceived() {
        this.isReceived = true;
    }
}
