package com.example.honjarang.domain.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@JsonNaming
public class PasswordUpdateDto {
    private String password;
    private String newPassword;
}
