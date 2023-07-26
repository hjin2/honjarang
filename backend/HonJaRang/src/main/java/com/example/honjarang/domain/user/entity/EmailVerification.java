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
    public EmailVerification(String email, String code, LocalDateTime expiredAt, Boolean isVerified) {
        this.email = email;
        this.code = code;
        this.expiredAt = expiredAt;
        this.isVerified = isVerified;
    }

    public void verify() {
        this.isVerified = true;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    public void setExpiredAtForTest(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public void setIsVerifiedForTest(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
