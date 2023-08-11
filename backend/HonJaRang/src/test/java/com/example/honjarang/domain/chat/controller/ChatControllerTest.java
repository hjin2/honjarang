package com.example.honjarang.domain.chat.controller;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.chat.document.ChatMessage;
import com.example.honjarang.domain.chat.dto.ChatMessageListDto;
import com.example.honjarang.domain.chat.dto.ChatRoomListDto;
import com.example.honjarang.domain.chat.entity.ChatRoom;
import com.example.honjarang.domain.chat.service.ChatService;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ChatController.class)
class ChatControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChatService chatService;

    private User user;
    private ChatRoom chatRoom;
    private ChatMessage chatMessage;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("honjarang.kro.kr")
                        .withPort(80))
                .build();
        user = User.builder()
                .email("test@test.com")
                .password("test1234")
                .nickname("테스트")
                .point(10000)
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .role(Role.ROLE_USER)
                .build();
        user.setIdForTest(1L);
        chatRoom = ChatRoom.builder()
                .name("테스트 채팅방")
                .build();
        chatRoom.setIdForTest(1L);
        chatMessage = ChatMessage.builder()
                .content("테스트 메시지")
                .chatRoomId(chatRoom.getId())
                .userId(user.getId())
                .build();
        chatMessage.setCreatedAtForTest(DateTimeUtils.parseInstant("2000-01-01 00:00:00"));
        chatMessage.setIdForTest(new ObjectId("5f9e9f9e9f9e9f9e9f9e9f9e"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    @DisplayName("채팅방 목록 조회")
    void getChatRoomList() throws Exception{
        // given
        ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom, chatMessage.getContent(), chatMessage.getCreatedAt(), 1);

        given(chatService.getChatRoomList(user)).willReturn(List.of(chatRoomListDto));

        // when & then
        mockMvc.perform(get("/api/v1/chats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("테스트 채팅방"))
                .andExpect(jsonPath("$[0].last_message").value("테스트 메시지"))
                .andExpect(jsonPath("$[0].last_message_created_at").exists())
                .andExpect(jsonPath("$[0].unread_message_count").value(1))
                .andDo(document("chat/getChatRoomList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("채팅방 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("채팅방 이름"),
                                fieldWithPath("[].last_message").type(JsonFieldType.STRING).description("채팅방 마지막 메시지"),
                                fieldWithPath("[].last_message_created_at").type(JsonFieldType.STRING).description("채팅방 마지막 메시지 작성 시간"),
                                fieldWithPath("[].unread_message_count").type(JsonFieldType.NUMBER).description("채팅방 안 읽은 메시지 수")
                        )
                ));
    }

    @Test
    @DisplayName("채팅방 메시지 목록 조회")
    void getChatMessageList() throws Exception {
        // given
        ChatMessageListDto chatMessageListDto = new ChatMessageListDto(chatMessage, user.getNickname());

        given(chatService.getChatMessageList(eq(1L), eq(1), eq(10), any(User.class))).willReturn(List.of(chatMessageListDto));

        // when & then
        mockMvc.perform(get("/api/v1/chats/{roomId}", 1L)
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("5f9e9f9e9f9e9f9e9f9e9f9e"))
                .andExpect(jsonPath("$[0].content").value("테스트 메시지"))
                .andExpect(jsonPath("$[0].user_id").value(1L))
                .andExpect(jsonPath("$[0].nickname").value("테스트"))
                .andExpect(jsonPath("$[0].created_at").exists())
                .andDo(document("chat/getChatMessageList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("roomId").description("채팅방 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.STRING).description("메시지 ID"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("메시지 내용"),
                                fieldWithPath("[].user_id").type(JsonFieldType.NUMBER).description("메시지 작성자 ID"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("메시지 작성자 닉네임"),
                                fieldWithPath("[].created_at").type(JsonFieldType.STRING).description("메시지 작성 시간")
                        )
                ));
    }

    @Test
    @DisplayName("채팅방 메시지 페이지 수 조회")
    void getChatMessagePage() throws Exception {
        // given
        given(chatService.getChatMessagePageCount(1L, 10)).willReturn(1 );

        // when & then
        mockMvc.perform(get("/api/v1/chats/{roomId}/page", 1L)
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1))
                .andDo(document("chat/getChatMessagePage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("roomId").description("채팅방 ID")
                        ),
                        queryParameters(
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseBody()
                ));
    }

    @Test
    @DisplayName("1:1 채팅방 생성")
    void createOneToOneChatRoom() throws Exception {
        // given
        Map<String, Object> body = new HashMap<>();
        body.put("target_id", 2L);

        // when & then
        mockMvc.perform(post("/api/v1/chats/one-to-one")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("chat/createOneToOneChatRoom",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("target_id").type(JsonFieldType.NUMBER).description("상대방 ID")
                        )
                ));
    }
}