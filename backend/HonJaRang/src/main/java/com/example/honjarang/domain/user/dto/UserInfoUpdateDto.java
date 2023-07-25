package com.example.honjarang.domain.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@JsonNaming
public class UserInfoUpdateDto {
    private String nickname;
    private String address;
}
