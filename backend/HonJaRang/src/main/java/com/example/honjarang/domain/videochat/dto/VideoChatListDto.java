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
    private Boolean onlyVoice;
    private String createdAt;
    private String title;
    private Integer count;
    private String thumbnail;

    public VideoChatListDto(VideoChatRoom videoChatRoom, Integer count) {
        this.id = videoChatRoom.getId();
        this.sessionId = videoChatRoom.getSessionId();
        this.category = videoChatRoom.getCategory();
        this.onlyVoice = videoChatRoom.getOnlyVoice();
        this.title = videoChatRoom.getTitle();
        this.createdAt = DateTimeUtils.formatLocalDateTime(videoChatRoom.getCreatedAt());
        this.count = count;
        this.thumbnail = videoChatRoom.getThumbnail();

    }

}
