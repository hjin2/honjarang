package com.example.honjarang.domain.chat.service;

import com.example.honjarang.domain.chat.document.ChatMessage;
import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import com.example.honjarang.domain.chat.dto.ChatMessageListDto;
import com.example.honjarang.domain.chat.repository.ChatMessageRepository;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final RabbitTemplate rabbitTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    private final static String CHAT_EXCHANGE_NAME = "amq.topic";
    private final static String CHAT_QUEUE_NAME = "chat.queue";
    public void sendChatMessageToQueue(Long roomId, ChatMessageCreateDto chatMessageCreateDto) {
        rabbitTemplate.convertAndSend("amq.topic", "room." + roomId, chatMessageCreateDto);
    }

    @Transactional
    public void createChatMessage(ChatMessageCreateDto chatMessageCreateDto) {
        ChatMessage chatMessage = chatMessageCreateDto.toEntity();
        chatMessageRepository.save(chatMessage);
    }

    @Cacheable(value = "chatMessageList", key = "#chatRoomId")
    @Transactional(readOnly = true)
    public List<ChatMessageListDto> getChatMessageList(Long chatRoomId) {
        List<ChatMessageListDto> chatMessageListDtoList = new ArrayList<>();
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoomId(chatRoomId);
        for(ChatMessage chatMessage : chatMessageList) {
            User user = userRepository.findById(chatMessage.getUserId()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
            ChatMessageListDto chatMessageListDto = new ChatMessageListDto(chatMessage, user.getNickname());
            chatMessageListDtoList.add(chatMessageListDto);
        }
        return chatMessageListDtoList;
    }
}
