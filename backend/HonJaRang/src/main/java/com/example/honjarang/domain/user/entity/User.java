package com.example.honjarang.domain.user.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profile_image;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ROLE_USER'")
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Builder
    public User(String email, String password, String nickname, String address, Double latitude, Double longitude) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}