package com.example.honjarang.domain.post.service;


import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.dto.PostDto;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.dto.PostUpdateDto;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.exception.*;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(PostCreateDto postCreateDto, User user) {
        if (postCreateDto.getTitle().equals(null) || postCreateDto.getTitle().equals("") || postCreateDto.getTitle().equals(" ")) {
            if (postCreateDto.getContent().equals(null) || postCreateDto.getContent().equals("") || postCreateDto.getTitle().equals(" ")) {
                throw new TitleAndContentEmptyException("제목과 내용은 필수 값입니다.");
            }
            else
                throw new TitleEmptyException("제목은 필수 값입니다.");
        }
        else if (postCreateDto.getContent().equals(null) || postCreateDto.getContent().equals("") || postCreateDto.getTitle().equals(" ")) {
            throw new ContentEmptyException("내용은 필수 값입니다.");
        }

        return postRepository.save(postCreateDto.toEntity(user)).getId();
    }

    @Transactional
    public void deletePost(long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("존재하지 않는 게시글입니다."));

        if (!Objects.equals(post.getUser().getId(),user.getId())) {
            throw new InvalidUserException("작성자만 삭제할 수 있습니다.");
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public void updatePost(Long id, PostUpdateDto postUpdateDto, User user) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("존재하지 않는 게시글입니다."));
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new InvalidUserException("작성자만 수정할 수 있습니다.");
        }
        post.update(postUpdateDto);
    }


    @Transactional(readOnly = true)
    public List<PostListDto> getPostList(int page, String keyword) {
        Pageable pageable = PageRequest.of(page -1, 15);
        return postRepository.findAllByTitleContainingIgnoreCaseOrderByIsNoticeDescIdDesc(keyword, pageable)
                .stream()
                .map(post -> toPostListDto(post))
                .toList();
    }

    @Transactional
    public PostDto getPost(long id, @CurrentUser User user) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
        postRepository.increaseViews(id);
        post.increaseViews();
        return toPostDto(post);

    }

    private PostListDto toPostListDto(Post post) {
        return new PostListDto(
                post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getCategory(),
                post.getContent(),
                post.getViews(),
                post.getIsNotice(),
                DateTimeUtils.formatLocalDateTime(post.getCreatedAt())
        );
    }

    private PostDto toPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getCategory(),
                post.getContent(),
                post.getViews(),
                post.getIsNotice(),
                DateTimeUtils.formatLocalDateTime(post.getCreatedAt()),
                DateTimeUtils.formatLocalDateTime(post.getUpdatedAt())
        );
    }
}
