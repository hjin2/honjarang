package com.example.honjarang.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageCreateDto {
    private String content;
    private String sender;
}
