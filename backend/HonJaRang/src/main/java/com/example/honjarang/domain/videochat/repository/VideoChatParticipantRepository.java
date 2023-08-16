package com.example.honjarang.domain.videochat.repository;

import com.example.honjarang.domain.videochat.entity.VideoChatParticipant;
import com.example.honjarang.domain.videochat.entity.VideoChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoChatParticipantRepository extends JpaRepository <VideoChatParticipant, Long> {

    VideoChatParticipant findByUserId(Long userId);

    Integer countByVideoChatRoom(VideoChatRoom videoChatRoom);
    
    void deleteByUserId(Long userId);
}
