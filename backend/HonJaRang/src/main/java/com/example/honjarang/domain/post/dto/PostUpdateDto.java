package com.example.honjarang.domain.post.dto;


import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDto {
    private Long id;
    private String title;
    private String content;
    private Boolean isNotice;
    private Category category;

    public Post toEntity(boolean isNotice) {
        return Post.builder()
                .title(title)
                .content(content)
                .isNotice(isNotice)
                .category(category)
                .build();
    }
}
