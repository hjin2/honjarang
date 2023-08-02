package com.example.honjarang.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Getter
@Entity
@DynamicInsert
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Builder
    public ChatRoom(String name) {
        this.name = name;
    }
}
