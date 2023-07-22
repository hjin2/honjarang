package com.example.honjarang.domain.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming
public class UserCreateDto {
    private String email;
    private String password;
    private String nickname;
    private String address;
    private Double latitude;
    private Double longitude;
}
