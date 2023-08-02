package com.example.honjarang.domain.post.service;


import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.post.dto.CommentCreateDto;
import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.dto.PostUpdateDto;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Comment;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.exception.*;
import com.example.honjarang.domain.post.repository.CommentRepository;
import com.example.honjarang.domain.post.repository.LikePostRepository;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Spy
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikePostRepository likePostRepository;

    @Mock
    private CommentRepository commentRepository;

    private Post post;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@test.com")
                .password("test1234")
                .nickname("테스트")
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .role(Role.ROLE_USER)
                .build();
        post = Post.builder()
                .title("test")
                .user(user)
                .views(0)
                .category(Category.FREE)
                .isNotice(false)
                .content("test")
                .build();
        post.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2023-08-02 12:00:00"));
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createPost_success() {
        // given

        PostCreateDto postCreateDto = new PostCreateDto("test", "test");

        // when & then
        postRepository.save(postCreateDto.toEntity(user));

    }


    @Test
    @WithMockUser
    @DisplayName("게시글 삭제 성공")
    void deletePost_Success() {

        // given
        Long postId = 1L;

        // when & then
        postRepository.deleteById(postId);
    }


    @Test
    @DisplayName("게시글 삭제 실패 - 게시글이 존재하지 않을 경우")
    void deletePost_EmptyPostException() {

        // given
        Long postId = 1L;
        
        // when & then
        assertThrows(PostNotFoundException.class, () -> postService.deletePost(postId, user));
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 작성자가 다를 경우")
    void deletePost_DisMatchUserException() {

        // given
        
        User invalidUser = User.builder()
                .email("234234")
                 .password("password")
                .build();

        doThrow(new InvalidUserException("사용자가 다릅니다.")).when(postService).deletePost(1L, invalidUser);
        // when
        assertThrows(InvalidUserException.class, () -> postService.deletePost(1L, invalidUser));
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePost_Success() {

        Post post = Post.builder()
                .title("test")
                .content("test")
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
        User invalidUser = User.builder()
                .email("invalid")
                .password("password")
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
        PostListDto postListDto = new PostListDto(post);
        List<PostListDto> postList = new ArrayList<>();
        postList.add(postListDto);

        when(postService.getPostList(testPage, testKeyword)).thenReturn(postList);

        // when & then
        assertThat(postService.getPostList(testPage, testKeyword)).isEqualTo((postList));
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void createComment_Success() {

        // given
        CommentCreateDto commentCreateDto = new CommentCreateDto("test");
        Comment comment = commentCreateDto.toEntity(post, user);
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        // when
        postService.createComment(1L, commentCreateDto, user);
    }

    @Test
    @DisplayName("댓글 작성 실패 - 게시글이 존재하지 않을 경우")
    void createComment_PostNotException() {

        // given
        CommentCreateDto commentCreateDto = new CommentCreateDto("test");
        given(postRepository.findById(1L)).willThrow(new PostNotFoundException("게시글이 존재하지 않습니다."));

        // when & then
        assertThrows(PostNotFoundException.class, () -> postService.createComment(1L, commentCreateDto, user));
    }

    @Test
    @DisplayName("댓글 작성 실패 - 댓글 내용이 없을 경우")
    void createComment_ContentEmptyException() {

        // given
        CommentCreateDto commentCreateDto = new CommentCreateDto("");
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        // WHEN & THEN
        assertThrows(ContentEmptyException.class, () -> postService.createComment(1L, commentCreateDto, user));

    }

    @Test
    @DisplayName("게시글 좋아요 성공")
    void togglePostLike_Success() {

        // given
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        // when
        postService.togglePostLike(1L, user);

        // then
        then(postRepository).should().findById(1L);
        then(likePostRepository).should().countByPostIdAndUserId(1L, user.getId());
    }

    @Test
    @DisplayName("게시글 좋아요 실패 - 게시글이 존재하지 않을 경우")
    void togglePostLike_PostNotFoundException() {

        // given
        given(postRepository.findById(1L)).willThrow(PostNotFoundException.class);

        // when & then
        assertThrows(PostNotFoundException.class, () -> postService.togglePostLike(1L, user));
    }

    @DisplayName("게시글 상세 조회 성공")
    void getPost_Success() {
        // given
        Long id = 1L;
        Post post = Post.builder()
                .title("test")
                .content("test")
                .build();

        given(postRepository.findById(id)).willReturn(Optional.of(post));

        // when & then
        assertThat(postRepository.findById(id)).isEqualTo(Optional.of(post));
    }

    @Test
    @DisplayName("게시글 상세 조회 실패 - 게시글이 존재하지 않을 경우")
    void getPost_PostNotFoundException() {

        // given
        Long id = 1L;

        // when & then
        assertThrows(PostNotFoundException.class, () -> postService.getPost(id));
    }

}