package com.example.honjarang.domain.chat.dto;

import com.example.honjarang.domain.chat.document.ChatMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMessageCreateDto {
    private String content;
    private Long roomId;
    private String sessionId;

    public ChatMessage toEntity(Long userId) {
        return ChatMessage.builder()
                .userId(userId)
                .content(content)
                .chatRoomId(roomId)
                .build();
    }
}
