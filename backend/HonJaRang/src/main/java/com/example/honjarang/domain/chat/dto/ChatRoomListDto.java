package com.example.honjarang.domain.chat.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.chat.entity.ChatRoom;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.Instant;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRoomListDto {
    private Long id;
    private String name;
    private String lastMessage;
    private String lastMessageCreatedAt;
    private Integer unreadMessageCount;

    public ChatRoomListDto(ChatRoom chatRoom, String lastMessage, Instant lastMessageCreatedAt, Integer unreadMessageCount) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
        this.lastMessage = lastMessage;
        this.lastMessageCreatedAt = lastMessageCreatedAt != null ? DateTimeUtils.formatInstant(lastMessageCreatedAt) : null;
        this.unreadMessageCount = unreadMessageCount;
    }
}
