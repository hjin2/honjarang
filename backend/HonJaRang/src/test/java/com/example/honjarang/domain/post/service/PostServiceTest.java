package com.example.honjarang.domain.post.service;


import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.post.dto.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    private Comment comment;

    private CommentListDto commentListDto;

    @Mock
    private S3Client s3Client;


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
        user.setIdForTest(1L);
        post = Post.builder()
                .title("test")
                .user(user)
                .views(0)
                .category(Category.FREE)
                .isNotice(false)
                .content("test")
                .build();
        post.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2023-08-02 12:00:00"));
        comment = Comment.builder()
                .post(post)
                .content("test")
                .user(user)
                .build();
        comment.setIdForTest(1L);
        comment.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2030-01-01 00:00:00"));
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createPost_success() throws IOException {
        // given
        MultipartFile multipartFile = new MockMultipartFile("test_image", "test_image".getBytes());
        String imageUrl = multipartFile.getOriginalFilename();
        PostCreateDto postCreateDto = new PostCreateDto("title",Category.FREE,"content");


        Post savePost = postCreateDto.toEntity(user,imageUrl);
        savePost.setIdForTest(1L);

        given(postRepository.save(any(Post.class))).willReturn(savePost);

        // when & then
        assertThat(postService.createPost(postCreateDto, multipartFile, user)).isEqualTo(1L);

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

//    @Test
//    @DisplayName("게시글 수정 성공")
//    void updatePost_Success() throws IOException {
//
//        // given
//        MultipartFile multipartFile = new MockMultipartFile("test_image", "test_image".getBytes());
//        String imageUrl = multipartFile.getOriginalFilename();
//
//        PostUpdateDto postUpdateDto = new PostUpdateDto(1L ,"수정 테스트 타이틀","수정 테스트 본문",Category.TIP,true);
//
//        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(post));
//
//        postService.updatePost(multipartFile, postUpdateDto, user);
//
//        // when & then
//        assertThat(post.getTitle()).isEqualTo("수정 테스트 타이틀");
//        assertThat(post.getContent()).isEqualTo("수정 테스트 본문");
//        assertThat(post.getCategory()).isEqualTo(Category.TIP);
//        assertThat(post.getIsNotice()).isEqualTo(true);
//
//    }

//    @Test
//    @DisplayName("게시글 수정 실패 - 존재하지 않는 게시글일 때")
//    void updatePost_PostNotFoundException() throws IOException {
//
//        // given
//        MultipartFile multipartFile = new MockMultipartFile("test_image", "test_image".getBytes());
//        String imageUrl = multipartFile.getOriginalFilename();
//        PostUpdateDto postUpdateDto = new PostUpdateDto(1L ,"수정 테스트 타이틀","수정 테스트 본문",Category.TIP,true);
//        given(postRepository.findById(any(Long.class))).willReturn(java.util.Optional.empty());
//
//        // when & then
//        assertThrows(PostNotFoundException.class, () -> postService.updatePost(multipartFile, postUpdateDto, user));
//
//
//    }

//    @Test
//    @DisplayName("게시글 수정 실패 - 작성자가 아닐때 수정 시도하는 경우")
//    void updatePost_InvalidUserException() throws IOException {
//
//        // given
//        MultipartFile multipartFile = new MockMultipartFile("test_image", "test_image".getBytes());
//        String imageUrl = multipartFile.getOriginalFilename();
//        PostUpdateDto postUpdateDto = new PostUpdateDto(1L ,"수정 테스트 타이틀","수정 테스트 본문",Category.TIP,true);
//        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(post));
//        User notWriter =  User.builder()
//                .email("test2@test.com")
//                .password("test12345")
//                .nickname("테스트1")
//                .address("서울특별시 강남구")
//                .latitude(37.1)
//                .longitude(127.1)
//                .role(Role.ROLE_USER)
//                .build();
//        notWriter.setIdForTest(3L);
//
//        // when & then
//        assertThrows(InvalidUserException.class, () -> postService.updatePost(multipartFile, postUpdateDto, notWriter));
//
//    }




    @Test
    @DisplayName("게시글 목록 조회 성공")
    void getPostList_Success() {
        // given
        Pageable pageable = PageRequest.of(0, 15);

        List<Post> posts=  List.of(post);

        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());
        given(postRepository.findAllByTitleContainingIgnoreCaseOrderByIsNoticeDescIdDesc("test", pageable)).willReturn(postPage);

        List<PostListDto> result = postService.getPostList(1,"test");

        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
        assertThat(result.get(0).getUserId()).isEqualTo(user.getId());
        assertThat(result.get(0).getViews()).isEqualTo(post.getViews());
        assertThat(result.get(0).getCategory()).isEqualTo(post.getCategory());
        assertThat(result.get(0).getIsNotice()).isEqualTo(post.getIsNotice());
        assertThat(result.get(0).getContent()).isEqualTo(post.getContent());


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
    @DisplayName("댓글 삭제 성공")
    void deleteComment_Success() {

        // given
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        // when
        postService.deleteComment(1L, user);
    }



    @Test
    @DisplayName("댓글 삭제 실패 - 존재하지 않는 댓글일 경우")
    void deleteComment_CommentNotFoundException() {

        // given
        given(commentRepository.findById(1L)).willThrow(new CommentNotFoundException("댓글이 존재하지 않습니다."));

        // when & then
        assertThrows(CommentNotFoundException.class, (() -> postService.deleteComment(1L, user)));

    }

    @Test
    @DisplayName("댓글 삭제 실패 - 작성자가 아닐 경우")
    void deleteComment_InvalidUserException() {

        // given
        User userForTest = User.builder().build();
        userForTest.setIdForTest(2L);
        Comment comment = Comment.builder().user(user).build();

        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when & then
        assertThrows(InvalidUserException.class, () -> postService.deleteComment(1L, userForTest));
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    void getCommentList_Success() {

        // given
        List<Comment> commentList= List.of(comment);
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(commentRepository.findAllByPostId(1L)).willReturn(commentList);

        // when
        List<CommentListDto> commentListDtoList = postService.getCommentList(1L);

        // then
        assertThat(commentListDtoList).isNotNull();
        assertThat(commentListDtoList.size()).isEqualTo(1);
        assertThat(commentListDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(commentListDtoList.get(0).getContent()).isEqualTo("test");
        assertThat(commentListDtoList.get(0).getUserId()).isEqualTo(1L);
        assertThat(commentListDtoList.get(0).getNickname()).isEqualTo("테스트");
        assertThat(commentListDtoList.get(0).getCreatedAt()).isEqualTo("2030-01-01 00:00:00");
    }

    @Test
    @DisplayName("댓글 목록 조회 실패 - 게시글이 존재하지 않을 경우")
    void getPostList_PostNotFoundExceptoin() {

        // given
        given(postRepository.findById(1L)).willThrow(new PostNotFoundException("게시글이 존재하지 않습니다."));

        // when & then
        assertThrows(PostNotFoundException.class, () -> postService.getCommentList(1L));

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