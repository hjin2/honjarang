package com.example.honjarang.domain.post.dto;


import com.example.honjarang.domain.post.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDto {
    private Long id;
    private Long userId;
    private String title;
    private Category category;
    private String content;
    private Integer views;
    private Boolean isNotice;
    private String createdAt;
    private String updatedAt;


}
