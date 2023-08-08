package com.example.honjarang.domain.videochat.entity;

import com.example.honjarang.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Category category;

    private Boolean isScreen;




}
