package com.example.honjarang.domain.chat.document;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Document
public class ChatMessage {
    @Id
    private ObjectId id;
    private String content;
    private Long chatRoomId;
    private Long userId;

    @CreatedDate
    private Instant createdAt;

    @Builder
    public ChatMessage(String content, Long chatRoomId, Long userId) {
        this.content = content;
        this.chatRoomId = chatRoomId;
        this.userId = userId;
    }

    public void setIdForTest(ObjectId id) {
        this.id = id;
    }

    public void setCreatedAtForTest(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
