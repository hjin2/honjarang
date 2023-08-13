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
    private Integer count;
    private String thumbnail;

    public VideoChatListDto(VideoChatRoom videoChatRoom, Integer count) {
        this.id = videoChatRoom.getId();
        this.sessionId = videoChatRoom.getSessionId();
        this.category = videoChatRoom.getCategory();
        this.isScreen = videoChatRoom.getOnlyVoice();
        this.createdAt = DateTimeUtils.formatLocalDateTime(videoChatRoom.getCreatedAt());
        this.count = count;
        this.thumbnail = videoChatRoom.getThumbnail();

    }

}
