package com.example.honjarang.domain.jointdelivery.entity;

import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
public class JointDeliveryCart {
    @Id
    private Long id;

    @Column(nullable = false)
    private String menuId;

    @JoinColumn(name = "joint_delivery_id")
    @ManyToOne
    private JointDelivery jointDelivery;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Integer quantity;

    @Builder
    public JointDeliveryCart(String menuId, Integer quantity, JointDelivery jointDelivery, User user) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.jointDelivery = jointDelivery;
        this.user = user;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }
}
