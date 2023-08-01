package com.example.honjarang.domain.post.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentListDto {
    private Long id;
    private String content;
    private String nickname;
    private Long userId;
    private String createdAt;
}
