package com.example.honjarang.domain.post.controller;


import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Long> createPost(@RequestBody PostCreateDto postCreateDto, @CurrentUser User user) {
        return ResponseEntity.created(null).body(postService.createPost(postCreateDto, user));
    }
}