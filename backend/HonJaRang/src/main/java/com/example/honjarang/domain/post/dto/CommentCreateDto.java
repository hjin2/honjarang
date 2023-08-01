package com.example.honjarang.domain.post.dto;

import com.example.honjarang.domain.post.entity.Comment;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    private String content;

    public Comment toEntity(Post post, User user) {
        return Comment.builder()
                .post(post)
                .user(user)
                .content(this.content)
                .build();
    }
}
