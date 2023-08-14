package com.example.honjarang.domain.videochat.dto;

import com.example.honjarang.domain.videochat.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class VideoChatRoomCreateDto {
    private Boolean onlyVoice;
    private String title;
    private Category category;
    private String customSessionId;

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        if (onlyVoice != null) map.put("onlyVoice", onlyVoice);
        if (title != null) map.put("title", title);
        if (category != null) map.put("category", category);
        if (customSessionId != null) map.put("customSessionId", customSessionId);
        return map;
    }
}
