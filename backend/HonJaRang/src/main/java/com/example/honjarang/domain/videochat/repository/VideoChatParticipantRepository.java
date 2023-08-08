package com.example.honjarang.domain.videochat.repository;

import com.example.honjarang.domain.videochat.entity.VideoChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoChatParticipantRepository extends JpaRepository <VideoChatParticipant, Long> {

    void deleteByUserId(Long userId);

}
