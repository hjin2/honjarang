package com.example.honjarang.domain.post.controller;


import com.example.honjarang.domain.post.dto.CommentCreateDto;
import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.dto.PostUpdateDto;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PostController.class)
@AutoConfigureRestDocs
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

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
                .role(Role.ROLE_ADMIN)
                .build();
        post = Post.builder()
                .title("test")
                .user(user)
                .views(0)
                .category(Category.FREE)
                .isNotice(false)
                .content("test")
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("create Post")
    void createPost_Success() throws Exception {

        String title = "제목";
        String content = "content";

        PostCreateDto postCreateDto = new PostCreateDto(title, content);
        // when & then
        mockMvc.perform(post("/api/v1/posts")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(postCreateDto)))
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser
    @DisplayName("게시글 작성 실패 - 제목이 없을 경우")
    void createPost_EmptyTitle() throws Exception {

        // given
        String content = "content";
        PostCreateDto postCreateDto = new PostCreateDto(null, content);

        // when & then
        mockMvc.perform(post("/api/v1/posts")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(postCreateDto)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 작성 실패 - 내용이 없을 경우")
    void createPost_EmptyContent() throws Exception {

        // given
        String title = "title";
        PostCreateDto postCreateDto = new PostCreateDto(title, null);

        // when & then
        mockMvc.perform(post("/api/v1/posts")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(postCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 작성 실패 - 내용과 제목이 없을 경우")
    void createPost_EmptyTitleAndContent() throws Exception {

        // given
        PostCreateDto postCreateDto = new PostCreateDto(null, null);

        // when & then
        mockMvc.perform(post("/api/v1/posts")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(postCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 삭제 성공")
    void deletePost_Success() throws Exception {

        // given
        Long postId = 1L;

        // when & then
        mockMvc.perform(delete("/api/v1/posts/"+postId).
                        contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(postId)))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    @DisplayName("게시글 수정 성공")
    void updatePost_Success() throws Exception {

        // given
        Long postId = 1L;
        String title = "제목1";
        String content = "content1";
        Boolean isNotice = true;
        Category category = Category.FREE;

        PostUpdateDto postUpdateDto = new PostUpdateDto(postId, title, content, isNotice, category);

        // when & then
        mockMvc.perform(patch("/api/v1/posts/"+postId)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(postUpdateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 목록 조회 성공")
    void getPostList_Success() throws Exception {

        // given

        // when & then
        mockMvc.perform(get("/api/v1/posts"))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void creaetComment_Success() throws Exception {

        // given
        CommentCreateDto commentCreateDto = new CommentCreateDto("test");

        // when & then
        mockMvc.perform(post("/api/v1/posts/{id}/comments", 1L)
                 .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(commentCreateDto)))
                .andExpect(status().isCreated())
                .andDo(document("posts/1/comments",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글")
                        )
                ));
    }
}
