package com.example.honjarang.domain.chat.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.chat.document.ChatMessage;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMessageListDto {
    private String id;
    private String content;
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private String createdAt;

    public ChatMessageListDto(ChatMessage chatMessage, User user) {
        this.id = chatMessage.getId().toString();
        this.content = chatMessage.getContent();
        this.userId = chatMessage.getUserId();
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImage();
        this.createdAt = DateTimeUtils.formatInstant(chatMessage.getCreatedAt());
    }
}
