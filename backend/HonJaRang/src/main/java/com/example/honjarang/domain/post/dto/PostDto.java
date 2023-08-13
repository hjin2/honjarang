package com.example.honjarang.domain.post.dto;


import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostDto {
    private Long id;
    private Long userId;
    private String title;
    private Category category;
    private String content;
    private String nickname;
    private Integer views;
    private Boolean isNotice;
    private String createdAt;
    private String postImage;
    private Boolean likeCnt;


    public PostDto(Post post, Boolean likeCnt){
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.views = post.getViews();
        this.isNotice = post.getIsNotice();
        this.createdAt = DateTimeUtils.formatLocalDateTime(post.getCreatedAt());
        if(post.getPostImage().equals("")){
            this.postImage = "";
        }else {
            this.postImage = "https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/postImage/" + post.getPostImage();
        }
        this.likeCnt= likeCnt;
    }
}

