package com.example.honjarang.domain.post.dto;


import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostUpdateDto {
    private Long id;
    private String title;
    private String content;
    private Category category;
    private Boolean isNotice;


    public Post toEntity(boolean isNotice) {
        return Post.builder()
                .title(title)
                .content(content)
                .isNotice(isNotice)
                .category(category)
                .build();
    }
}
