package com.example.honjarang.domain.videochat.repository;

import com.example.honjarang.domain.videochat.entity.Category;
import com.example.honjarang.domain.videochat.entity.VideoChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoChatRoomRepository extends JpaRepository<VideoChatRoom, Long> {
    VideoChatRoom findBySessionId(String sessionId);

    List<VideoChatRoom> findByCategory(Category category);
}
