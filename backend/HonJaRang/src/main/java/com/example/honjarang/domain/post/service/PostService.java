package com.example.honjarang.domain.post.service;


import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(PostCreateDto postCreateDto, User user) {
        return postRepository.save(postCreateDto.toEntity(user)).getId();
    }

}
