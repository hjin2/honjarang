package com.example.honjarang.domain.post.dto;

import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PostCreateDto {

    private String title;
    private Category category;
    private String content;


    public Post toEntity(User user, String postImage) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .category(this.category)
                .postImage(postImage)
                .build();
    }
}
