package com.example.honjarang.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyCodeDto {
    private String email;
    private String code;
}
