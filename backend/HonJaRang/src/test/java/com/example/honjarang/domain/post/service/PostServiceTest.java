package com.example.honjarang.domain.post.service;


import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.dto.PostUpdateDto;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.exception.*;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.amazonaws.services.simpleemail.model.TlsPolicy.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Spy
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    private static final String TEST_TITLE = "title";
    private static final String TEST_CONTENT = "content";
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "test1234";

    @Test
    @DisplayName("게시글 작성 성공")
    void createPost_success() {
        // given

        PostCreateDto postCreateDto = new PostCreateDto(TEST_TITLE, TEST_CONTENT);

        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        Post post = Post.builder()
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .user(user)
                .build();

        // when & then
        postRepository.save(postCreateDto.toEntity(user));

    }

    @Test
    @DisplayName("게시글 작성 실패 - 제목이 없을 경우")
    void createPost_EmptyTitleException() {
        // given

        PostCreateDto postCreateDto = new PostCreateDto("", TEST_CONTENT);

        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        Post post = Post.builder()
                .title(null)
                .content(TEST_CONTENT)
                .user(user)
                .build();

        // when & then
        assertThrows(TitleEmptyException.class, () -> postService.createPost(postCreateDto,user));

    }

    @Test
    @DisplayName("게시글 작성 실패 - 내용이 없을 경우")
    void createPost_EmptyContentException() {
        // given

        PostCreateDto postCreateDto = new PostCreateDto(TEST_TITLE, "");

        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        Post post = Post.builder()
                .title(null)
                .content(TEST_CONTENT)
                .user(user)
                .build();

        // when & then
        assertThrows(ContentEmptyException.class, () -> postService.createPost(postCreateDto,user));
    }


    @Test
    @DisplayName("게시글 작성 실패 - 제목과 내용이 없을 경우")
    void createPost_EmptyTitleAndContentException() {
        // given

        PostCreateDto postCreateDto = new PostCreateDto("", "");

        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        Post post = Post.builder()
                .title(null)
                .content(TEST_CONTENT)
                .user(user)
                .build();

        // when & then
        assertThrows(TitleAndContentEmptyException.class, () -> postService.createPost(postCreateDto,user));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 삭제 성공")
    void deletePost_Success() {

        // given
        Long postId = 1L;
        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        // when & then
        postRepository.deleteById(postId);
    }


    @Test
    @DisplayName("게시글 삭제 실패 - 게시글이 존재하지 않을 경우")
    void deletePost_EmptyPostException() {

        // given
        Long postId = 1L;
        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        // when & then
        assertThrows(PostNotFoundException.class, () -> postService.deletePost(postId, user));
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 작성자가 다를 경우")
    void deletePost_DisMatchUserException() {

        // given
        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
        Long postId = 1L;
        Post post = Post.builder()
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .user(user)
                .build();

        User invalidUser = User.builder()
                .email("234234")
                 .password(TEST_PASSWORD)
                .build();

        doThrow(new InvalidUserException("사용자가 다릅니다.")).when(postService).deletePost(postId, invalidUser);
        // when
        assertThrows(InvalidUserException.class, () -> postService.deletePost(postId, invalidUser));
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePost_Success() {

        // given
        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        Post post = Post.builder()
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .build();

        String testTitle = "testtest";
        String testContent = "contentcontent";

        PostUpdateDto postUpdateDto = new PostUpdateDto(post.getId(), testTitle, testContent,
                post.getIsNotice(), post.getCategory());

        // when
        post.update(postUpdateDto);

        // then
        assertThat(post.getId()).isEqualTo(postUpdateDto.getId());
        assertThat(post.getTitle()).isEqualTo(postUpdateDto.getTitle());
        assertThat(post.getContent()).isEqualTo(postUpdateDto.getContent());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 작성자가 아닐 경우")
    void updatePost_DisMatchUserException() {

        // given
        User user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        Post post = Post.builder()
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .build();

        User invalidUser = User.builder()
                .email("invalid")
                .password(TEST_PASSWORD)
                .build();

        String testTitle = "testtest";
        String testContent = "contentcontent";

        PostUpdateDto postUpdateDto = new PostUpdateDto(post.getId(), testTitle, testContent,
                post.getIsNotice(), post.getCategory());
        
        doThrow(new InvalidUserException("작성자가 아닙니다.")).when(postService).
                updatePost(post.getId(), postUpdateDto, invalidUser);

        // when & 소
        assertThrows(InvalidUserException.class, () -> postService.updatePost(post.getId(), postUpdateDto, invalidUser));

    }

    @Test
    @DisplayName("게시글 목록 조회 성공")
    void getPostList_Success() {


        // given
        Integer testPage = 1;
        String testKeyword = "kk";
        PostListDto postListDto = new PostListDto(1L, 1L, "title",
                Category.FREE, "content", 1, false, DateTimeUtils.formatLocalDateTime(LocalDateTime.now()));
        List<PostListDto> postList = new ArrayList<>();
        postList.add(postListDto);

        when(postService.getPostList(testPage, testKeyword)).thenReturn(postList);
        // when & then
        assertThat(postService.getPostList(testPage, testKeyword)).isEqualTo((postList));
    }


}