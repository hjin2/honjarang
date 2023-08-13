package com.example.honjarang.domain.videochat.dto;

import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.videochat.entity.VideoChatParticipant;
import com.example.honjarang.domain.videochat.entity.VideoChatRoom;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VideoChatParticipantCreateDto {

    private VideoChatRoom videoChatRoom;
    private Boolean IsDeleted;

    public VideoChatParticipant toEntity(VideoChatParticipantCreateDto dto, User user) {
        return VideoChatParticipant.builder()
                .videoChatRoom(dto.getVideoChatRoom())
                .user(user)
                .isDeleted(dto.getIsDeleted())
                .build();

    }
}
