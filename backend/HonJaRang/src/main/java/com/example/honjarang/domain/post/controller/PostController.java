package com.example.honjarang.domain.post.controller;


import com.example.honjarang.domain.post.dto.*;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Long> createPost(@RequestPart(value = "post_image", required = false) MultipartFile postImage, @ModelAttribute(name = "postCreateDto") PostCreateDto postCreateDto,  @CurrentUser User user) throws IOException {
        Long result = postService.createPost(postCreateDto, postImage, user);
        return ResponseEntity.status(201).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deletePost(@PathVariable long id, @CurrentUser User user) {
        postService.deletePost(id, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePost(@RequestPart(value = "post_image", required = false) MultipartFile postImage,
                                           @ModelAttribute PostUpdateDto postUpdateDto, @PathVariable Long id, @CurrentUser User user) throws IOException {
        postService.updatePost(postImage, postUpdateDto, user, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public List<PostListDto> getPosts(@RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return postService.getPostList(page, keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id) {
        PostDto postDto = postService.getPost(id);
        return ResponseEntity.ok(postDto);
    }

    @PostMapping("/{id}/like")
    public void likePost(@PathVariable Long id, @CurrentUser User user) {
        postService.togglePostLike(id, user);
    }

    @PostMapping(value = "/{id}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long id, @RequestBody CommentCreateDto commentCreateDto, @CurrentUser User user) {
        postService.createComment(id, commentCreateDto, user);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping(value = "/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @CurrentUser User user) {
        postService.deleteComment(commentId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/comments")
    public List<CommentListDto> getComments(@PathVariable Long id) {
        return postService.getCommentList(id);
    }

    @GetMapping("/page")
    public ResponseEntity<Integer> getPostsPageCount(@RequestParam Integer size) {
        return ResponseEntity.ok(postService.getPostsPageCount(size));
    }

    @PutMapping("/{id}/notice")
    public ResponseEntity<Void> noticePost(@RequestParam Long id, @CurrentUser User user) {
        postService.noticePost(id, user);
        return ResponseEntity.ok().build();
    }

}