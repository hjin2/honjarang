package com.example.honjarang.domain.chat.controller;


import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import com.example.honjarang.domain.chat.dto.ChatMessageListDto;
import com.example.honjarang.domain.chat.dto.ChatMessageSendDto;
import com.example.honjarang.domain.chat.dto.ChatRoomListDto;
import com.example.honjarang.domain.chat.service.ChatService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("chat/message.{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageSendDto chatMessageCreateDto, SimpMessageHeaderAccessor headerAccessor) {
        chatService.sendChatMessageToQueue(roomId, chatMessageCreateDto, headerAccessor.getSessionId());
    }

    @MessageMapping("chat/connect.{roomId}")
    public void connectWebSocket(@DestinationVariable Long roomId, @Payload Map<String, Object> payload, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String token = (String) payload.get("token");
        chatService.connectChatRoom(roomId, sessionId, token);
    }

    @GetMapping("")
    public List<ChatRoomListDto> getChatRoomList(@CurrentUser User user) {
        return chatService.getChatRoomList(user);
    }

    @GetMapping("/{roomId}")
    public List<ChatMessageListDto> getChatMessageList(@PathVariable Long roomId, @RequestParam Integer page, @RequestParam Integer size, @CurrentUser User user) {
        return chatService.getChatMessageList(roomId, page, size, user);
    }


    @GetMapping("/{roomId}/page")
    public Integer getChatMessagePage(@PathVariable Long roomId, @RequestParam Integer size) {
        return chatService.getChatMessagePage(roomId, size);
    }
}
