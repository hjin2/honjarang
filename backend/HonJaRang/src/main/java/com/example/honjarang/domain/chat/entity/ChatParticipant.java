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
    @ColumnDefault("'000000000000000000000000'")
    private String lastReadMessageId;

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

    public void reEnter() {
        this.isDeleted = false;
        this.deletedAt = null;
    }

    public void exit() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateLastReadMessageId(String lastReadMessageId) {
        if(this.lastReadMessageId.compareTo(lastReadMessageId) < 0) {
            this.lastReadMessageId = lastReadMessageId;
        }
    }

    public void setLastReadMessageIdForTest(String lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
}
