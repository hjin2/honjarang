package com.example.honjarang.domain.post.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@DynamicInsert
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;

    }

    public void setUserForTest(User user) {
        this.user = user;
    }

    public void setIdForTest(Long id) { this.id = id; }
}
