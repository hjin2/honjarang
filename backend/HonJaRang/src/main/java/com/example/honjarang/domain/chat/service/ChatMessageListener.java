package com.example.honjarang.domain.chat.service;


import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageListener {
    private final static String CHAT_QUEUE_NAME = "chat.queue";
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void handleMessage(ChatMessageCreateDto chatMessage) {
        // 수신한 메시지 처리 로직을 작성합니다.
        System.out.println("Received message: " + chatMessage.getContent());
    }
}
