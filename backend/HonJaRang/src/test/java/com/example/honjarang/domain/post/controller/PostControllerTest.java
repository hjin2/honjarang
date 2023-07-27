package com.example.honjarang.domain.post.controller;


import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.dto.PostUpdateDto;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;


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
}
