package com.example.honjarang.domain.jointpurchase.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.chat.entity.ChatRoom;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@DynamicInsert
@NoArgsConstructor
@Entity
public class JointPurchase extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "chat_room_id")
    @OneToOne
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private Integer targetPersonCount;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer deliveryCharge;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    @ColumnDefault("false")
    private Boolean isCanceled;

    @Builder
    public JointPurchase(User user, String content, LocalDateTime deadline, Integer targetPersonCount, String productName, String image, Integer price, Integer deliveryCharge, String placeName, Double latitude, Double longitude, ChatRoom chatRoom) {
        this.user = user;
        this.content = content;
        this.deadline = deadline;
        this.targetPersonCount = targetPersonCount;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.deliveryCharge = deliveryCharge;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chatRoom = chatRoom;
    }

    public void cancel() {
        this.isCanceled = true;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    public void setCanceledForTest(Boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public void setDeadlineForTest(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
