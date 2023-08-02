package com.example.honjarang.domain.chat.service;

import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final RabbitTemplate rabbitTemplate;

    private final static String CHAT_EXCHANGE_NAME = "amq.topic";
    private final static String CHAT_QUEUE_NAME = "chat.queue";
    public void sendChatMessageToQueue(Long roomId, ChatMessageCreateDto chatMessageCreateDto) {
        rabbitTemplate.convertAndSend("amq.topic", "room." + roomId, chatMessageCreateDto);
    }

    @Transactional
    public void sendMessage(Long roomId, String message) {
       log.info("메시지 전송");
    }
}
