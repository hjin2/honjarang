package com.example.honjarang.domain.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
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
