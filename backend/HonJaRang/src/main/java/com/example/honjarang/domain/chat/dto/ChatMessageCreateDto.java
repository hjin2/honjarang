package com.example.honjarang.domain.chat.dto;

import com.example.honjarang.domain.chat.document.ChatMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMessageCreateDto {
    private String content;
    private Long roomId;
    private Long userId;

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .content(content)
                .chatRoomId(roomId)
                .userId(userId)
                .build();
    }
}
