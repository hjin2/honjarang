package com.example.honjarang.domain.videochat.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Getter
@Entity
@DynamicInsert
public class VideoChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sessionId;

    @Column(nullable = false)
    private Category category;

    private Boolean onlyVoice;

    private String title;

    private String thumbnail;

    @Builder
    public VideoChatRoom(String sessionId, Category category, Boolean onlyVoice, String title, String thumbnail) {
        this.sessionId = sessionId;
        this.category = category;
        this.onlyVoice = onlyVoice;
        this.title = title;
        this.thumbnail = thumbnail;
    }




}
