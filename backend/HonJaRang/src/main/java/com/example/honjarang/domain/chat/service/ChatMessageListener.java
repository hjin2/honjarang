package com.example.honjarang.domain.chat.service;


import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatMessageListener {
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    private final ChatService chatService;

    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void handleMessage(ChatMessageCreateDto chatMessage) {
        chatService.createChatMessage(chatMessage);
    }
}
