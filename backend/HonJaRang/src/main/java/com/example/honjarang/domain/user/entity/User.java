package com.example.honjarang.domain.user.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Getter
@Setter
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
    private String profileImage;

    @Column
    @ColumnDefault("0")
    private Integer point;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private Role role;

    @Column
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Builder
    public User(String email, String password, String nickname, String address, Double latitude, Double longitude, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.role = role;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    public void changePassword(String newPassword){
        this.password = newPassword;
    }

    public void changeUserInfo(String nickname, String address){
        this.nickname = nickname;
        this.address = address;
    }
}