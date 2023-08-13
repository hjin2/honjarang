package com.example.honjarang.domain.videochat.dto;

import com.example.honjarang.domain.videochat.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class VideoChatRoomCreateDto {
    private Boolean onlyVoice;
    private String title;
    private Category category;
    private String customSessionId;

    public Map<String, Object> convertToMap () {
        return Map.of(
                "onlyVoice", onlyVoice,
                "title", title,
                "category", category,
                "customSessionId", customSessionId
        );
    }
}
