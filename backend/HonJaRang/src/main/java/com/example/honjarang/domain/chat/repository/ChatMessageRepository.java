package com.example.honjarang.domain.chat.repository;

import com.example.honjarang.domain.chat.document.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, ObjectId> {
    List<ChatMessage> findAllByChatRoomId(Long chatRoomId);
}
