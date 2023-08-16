package com.example.honjarang.domain.videochat.repository;

import com.example.honjarang.domain.videochat.entity.Category;
import com.example.honjarang.domain.videochat.entity.VideoChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoChatRoomRepository extends JpaRepository<VideoChatRoom, Long> {
    VideoChatRoom findBySessionId(String sessionId);

    Integer findAllByCategoryAndTitleContainingIgnoreCase(Category category, String keyword);

    Page<VideoChatRoom> findAllByCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(Category option, String keyword, Pageable pageable);

}
