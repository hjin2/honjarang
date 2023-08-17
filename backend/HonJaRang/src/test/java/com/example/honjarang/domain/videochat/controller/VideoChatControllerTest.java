package com.example.honjarang.domain.videochat.controller;


import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.post.dto.CommentListDto;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.videochat.dto.VideoChatRoomCreateDto;
import com.example.honjarang.domain.videochat.service.VideoChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(VideoChatController.class)
@AutoConfigureRestDocs
public class VideoChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoChatService videoChatService;

    private VideoChatRoomCreateDto videoChatRoomCreateDto;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("honjarang.kro.kr")
                        .withPort(80))
                .build();
    }

//    @Test
//    @WithMockUser
//    @DisplayName("화상채팅 방 생성")
//    void InitializeSession_Success() throws Exception {
//        // given
//        MockMultipartFile file = new MockMultipartFile("video_chat_room_image", "test.jpg", "image/jpeg", "test".getBytes());
//        VideoChatRoomCreateDto videoChatRoomCreateDto = new VideoChatRoomCreateDto();
//        given(videoChatService.initializeSession(any(), any())).willReturn("abcde");
//
//        // when & then
//        mockMvc.perform(multipart("/api/v1/video-room/sessions")
//                        .file("video_chat_room_image", file.getBytes())
//                        .part(new MockPart("title", "test".getBytes(StandardCharsets.UTF_8)))
//                        .part(new MockPart("category", "FREE".getBytes(StandardCharsets.UTF_8)))
//                        .part(new MockPart("customSessionId", "abcde".getBytes(StandardCharsets.UTF_8)))
//                        .part(new MockPart("onlyVoice","false".getBytes(StandardCharsets.UTF_8))))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value("abcde"))
//                .andDo(document("video-chat/create",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestParts(
//                                partWithName("video_chat_room_image").description("화상채팅방 썸네일"),
//                                partWithName("title").description("화상채팅방 제목"),
//                                partWithName("category").description("화상채팅방 카테고리"),
//                                partWithName("customSessionId").description("화상채팅방 세션 ID"),
//                                partWithName("onlyVoice").description("음성방 여부")
//                        ),
//                        responseFields(
//                                fieldWithPath("sessionId").type(JsonFieldType.STRING).description("화상채팅방 세션 ID")
//                        )
//                ));
//
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("화상채팅 방 연결")
//    voi





}
