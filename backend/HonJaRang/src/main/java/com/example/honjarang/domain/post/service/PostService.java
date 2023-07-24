package com.example.honjarang.domain.post.service;


import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.exception.ContentEmptyException;
import com.example.honjarang.domain.post.exception.TitleAndContentEmptyException;
import com.example.honjarang.domain.post.exception.TitleEmptyException;
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

}
