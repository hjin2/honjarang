package com.example.honjarang.domain.post.controller;


import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.dto.PostDto;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.dto.PostUpdateDto;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Long> createPost(@Valid @RequestBody PostCreateDto postCreateDto, @CurrentUser User user) {
        return ResponseEntity.created(null).body(postService.createPost(postCreateDto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deletePost(@PathVariable long id, @CurrentUser User user) {
        postService.deletePost(id, user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> updatePost(@PathVariable Long id,
                                           @RequestBody PostUpdateDto postUpdateDto, @CurrentUser User user) {
        postService.updatePost(id, postUpdateDto, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public List<PostListDto> getPosts(@RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return postService.getPostList(page, keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable long id) {
        PostDto postDto = postService.getPost(id);
        return ResponseEntity.ok(postDto);
    }
}