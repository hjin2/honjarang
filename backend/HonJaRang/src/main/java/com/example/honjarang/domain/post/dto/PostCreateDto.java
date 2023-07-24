package com.example.honjarang.domain.post.dto;

import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDto {

    private String title;
    private String content;

    public PostCreateDto() {

    }
    public PostCreateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
