package com.example.honjarang.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentListDto {
    private Long id;
    private String content;
    private Long userId;
    private String nickname;
    private String createdAt;
}
