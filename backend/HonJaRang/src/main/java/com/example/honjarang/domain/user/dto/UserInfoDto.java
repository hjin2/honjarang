package com.example.honjarang.domain.user.dto;

import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoDto {
    private String nickname;
    private String email;
    private String profileImage;
    private Integer point;
    private String address;


    public UserInfoDto(User user){
       this.nickname = user.getNickname();
       this.email = user.getEmail();
       this.profileImage = user.getProfileImage();
       this.point = user.getPoint();
       this.address = user.getAddress();
    }
}
