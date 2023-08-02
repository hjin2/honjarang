package com.example.honjarang.domain.post.dto;

import com.example.honjarang.domain.post.entity.Category;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
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


    public PostListDto(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.views = post.getViews();
        this.isNotice = post.getIsNotice();
        this.createdAt = DateTimeUtils.formatLocalDateTime(post.getCreatedAt());
    }
}
