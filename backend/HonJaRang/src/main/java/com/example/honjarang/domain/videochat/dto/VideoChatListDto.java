package com.example.honjarang.domain.videochat.dto;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.videochat.entity.Category;
import com.example.honjarang.domain.videochat.entity.VideoChatRoom;
import lombok.Getter;

@Getter
public class VideoChatListDto {
    private Long id;
    private String sessionId;
    private Category category;
    private Boolean isScreen;
    private String createdAt;

    public VideoChatListDto(VideoChatRoom videoChatRoom) {
        this.id = videoChatRoom.getId();
        this.sessionId = videoChatRoom.getSessionId();
        this.category = videoChatRoom.getCategory();
        this.isScreen = videoChatRoom.getIsScreen();
        this.createdAt = DateTimeUtils.formatLocalDateTime(videoChatRoom.getCreatedAt());

    }

}
