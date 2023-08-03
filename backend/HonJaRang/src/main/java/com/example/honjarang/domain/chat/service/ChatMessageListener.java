package com.example.honjarang.domain.chat.service;


import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import com.example.honjarang.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatMessageListener {
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    private final ChatService chatService;


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
       chatService.disconnectChatRoom(event.getSessionId());
    }

    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void handleMessage(ChatMessageCreateDto chatMessageCreateDto) {
        chatService.createChatMessage(chatMessageCreateDto);
    }
}
