package com.example.honjarang.domain.post.repository;

import com.example.honjarang.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByTitleContainingIgnoreCaseOrderByIsNoticeDescIdDesc(String keyword, Pageable pageable);
    List<Post> findAllByUserIdOrderByIdDesc(long userId, Pageable pageable);
    List<Post> findAllByIsNoticeTrueOrderByIdDesc(Pageable pageable);

}
