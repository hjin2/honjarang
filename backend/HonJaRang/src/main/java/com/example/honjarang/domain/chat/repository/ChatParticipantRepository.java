package com.example.honjarang.domain.chat.repository;

import com.example.honjarang.domain.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    Optional<ChatParticipant> findByChatRoomIdAndUserIdAndIsDeletedIsFalse(Long chatRoomId, Long userId);
    Optional<ChatParticipant> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    List<ChatParticipant> findAllByUserIdAndIsDeletedIsFalse(Long userId);

    List<ChatParticipant> findAllByChatRoomIdAndIsDeletedIsFalse(Long chatRoomId);

    @Modifying
    void deleteByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
