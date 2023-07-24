package com.example.honjarang.domain.post.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@DynamicInsert
@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id", nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name="is_notice", columnDefinition ="TINYINT", nullable = false)
    @ColumnDefault("false")
    private Boolean isNotice;

    @Column(name="views", nullable = false)
    @ColumnDefault("0")
    private Integer views;

    @Column(nullable = false)
    @ColumnDefault("sdfs")
    private String category;
    @Builder
    public Post(User user, String category, String title, Boolean isNotice, String content, int views ) {
        this.category = category;
        this.title = title;
        this.user = user;
        this.isNotice = isNotice;
        this.content = content;
        this.views = views;
    }

    public void IncreaseViews() { this.views++; }

}
