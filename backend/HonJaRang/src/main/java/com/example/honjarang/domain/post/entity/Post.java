package com.example.honjarang.domain.post.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.post.dto.PostUpdateDto;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isNotice;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer views;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Builder
    public Post(User user, Category category, String title, Boolean isNotice, String content, Integer views) {
        this.category = category;
        this.title = title;
        this.user = user;
        this.isNotice = isNotice;
        this.content = content;
        this.views = views;
    }

    public void increaseViews() {
        this.views++;
    }

    public void update(PostUpdateDto postUpdateDto) {
        this.title = postUpdateDto.getTitle();
        this.content = postUpdateDto.getContent();
        this.isNotice = postUpdateDto.getIsNotice();
        this.category = postUpdateDto.getCategory();
    }
}
