package com.example.honjarang.domain.post.repository;

import com.example.honjarang.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("update Post p set p.views = p.views + 1 where p.id = :id")
    void increaseViews(@Param("id") long id);
    Page<Post> findAllByTitleContainingIgnoreCaseOrderByIsNoticeDescIdDesc(String keyword, Pageable pageable);
    Page<Post> findAllByUserIdOrderByIdDesc(long userId, Pageable pageable);
    List<Post> findAllByIsNoticeTrueOrderByIdDesc(Pageable pageable);

}
