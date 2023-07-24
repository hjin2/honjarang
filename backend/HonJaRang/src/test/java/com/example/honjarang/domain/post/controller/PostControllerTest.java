package com.example.honjarang.domain.post.controller;


import com.example.honjarang.domain.post.dto.PostCreateDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
