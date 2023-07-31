package com.example.honjarang.domain.post.dto;

import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {

    private String title;

    private String content;

    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
