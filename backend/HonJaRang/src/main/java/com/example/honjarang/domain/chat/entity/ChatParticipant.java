package com.example.honjarang.domain.chat.entity;

import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@DynamicInsert
@Entity
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "chat_room_id")
    @ManyToOne
    private ChatRoom chatRoom;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column
    private LocalDateTime deletedAt;

    @Column
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Builder
    public ChatParticipant(ChatRoom chatRoom, User user) {
        this.chatRoom = chatRoom;
        this.user = user;
    }

    public void exit() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
