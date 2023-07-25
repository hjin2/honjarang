package com.example.honjarang.domain.post.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostDto {
    private long id;
    private long userId;
    private String title;
    private String category;
    private String content;
    private int views;
    private boolean isNotice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
