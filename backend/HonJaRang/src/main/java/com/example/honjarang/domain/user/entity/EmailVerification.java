package com.example.honjarang.domain.user.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@DynamicInsert
@Getter
@NoArgsConstructor
@Entity
public class EmailVerification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column
    @ColumnDefault("false")
    private Boolean isVerified;

    @Builder
    public EmailVerification(String email, String code, LocalDateTime expiredAt) {
        this.email = email;
        this.code = code;
        this.expiredAt = expiredAt;
    }

    public void verify() {
        this.isVerified = true;
    }
}
