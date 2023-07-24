package com.example.honjarang.domain.post.service;


import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

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
}
