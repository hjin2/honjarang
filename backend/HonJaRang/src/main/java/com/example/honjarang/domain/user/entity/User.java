package com.example.honjarang.domain.user.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@DynamicInsert
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
    public User(String email, String password, String nickname, Integer point, String address, Double latitude, Double longitude, Role role, String profileImage, Boolean isDeleted) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.point = point;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.role = role;
        this.profileImage = profileImage;
        this.isDeleted = isDeleted;
    }

    public void changePassword(String newPassword){
        this.password = newPassword;
    }

    public void changeUserInfo(String nickname, String address, Double latitude, Double longitude){
        this.nickname = nickname;
        this.address = address;
        this.latitude = latitude;
        this.longitude=longitude;
    }

    public void changeProfileImage(String profileImage){
        this.profileImage = profileImage;
    }

    public void addPoint(Integer point) {
        this.point += point;
    }

    public void subtractPoint(Integer point) {
        this.point -= point;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    public void setIsDeletedForTest(Boolean isDeleted){
        this.isDeleted = isDeleted;
    }


    public void changePoint(Integer point) {this.point = point;}

    public void deleteUser(){
        this.isDeleted = true;
        this.email = "";
        this.address = "";
        this.latitude = 0D;
        this.longitude =0D;
        this.profileImage="";
    }

    public void setPointForTest(Integer point){this.point = point;}
}