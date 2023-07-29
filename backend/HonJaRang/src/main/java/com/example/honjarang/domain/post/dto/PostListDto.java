package com.example.honjarang.domain.post.dto;

import com.example.honjarang.domain.post.entity.Category;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostListDto {
    private Long id;
    private Long userId;
    private String title;
    private Category category;
    private String content;
    private Integer views;
    private Boolean isNotice;
    private String createdAt;
}
