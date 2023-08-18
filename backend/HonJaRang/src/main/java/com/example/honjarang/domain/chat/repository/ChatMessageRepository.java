package com.example.honjarang.domain.chat.repository;

import com.example.honjarang.domain.chat.document.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, ObjectId> {
    Page<ChatMessage> findAllByChatRoomIdOrderByCreatedAt(Long chatRoomId, Pageable pageable);
    Integer countAllByChatRoomIdAndIdGreaterThan(Long chatRoomId, ObjectId lastMessageId);

    Optional<ChatMessage> findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    Integer countAllByChatRoomId(Long chatRoomId);
}
