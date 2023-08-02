package com.example.honjarang.domain.chat.controller;


import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import com.example.honjarang.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("chat.message.{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageCreateDto chatMessageCreateDto) {
        chatService.sendChatMessageToQueue(roomId, chatMessageCreateDto);
    }
}
