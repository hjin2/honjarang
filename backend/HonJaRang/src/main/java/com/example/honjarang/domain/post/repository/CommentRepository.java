package com.example.honjarang.domain.post.repository;

import com.example.honjarang.domain.post.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);

    List<Comment> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    int countByPostId(Long postId);

    int countByUserId(Long id);

}
