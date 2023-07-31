package com.example.honjarang.domain.post.controller;


import com.example.honjarang.domain.post.dto.CommentCreateDto;
import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.dto.PostUpdateDto;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping(value = "/{id}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long id, @RequestBody CommentCreateDto commentCreateDto, @CurrentUser User user) {
        postService.createComment(id, commentCreateDto, user);
        return ResponseEntity.status(201).build();
    }

}