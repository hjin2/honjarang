package com.example.honjarang.domain.chat.service;

import com.example.honjarang.domain.chat.document.ChatMessage;
import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import com.example.honjarang.domain.chat.dto.ChatMessageListDto;
import com.example.honjarang.domain.chat.dto.ChatRoomListDto;
import com.example.honjarang.domain.chat.entity.ChatParticipant;
import com.example.honjarang.domain.chat.entity.ChatRoom;
import com.example.honjarang.domain.chat.exception.ChatParticipantNotFoundException;
import com.example.honjarang.domain.chat.repository.ChatMessageRepository;
import com.example.honjarang.domain.chat.repository.ChatParticipantRepository;
import com.example.honjarang.domain.chat.repository.ChatRoomRepository;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final RabbitTemplate rabbitTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
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

    @Transactional
    public List<ChatMessageListDto> getChatMessageList(Long chatRoomId, Integer page, Integer size, User loginUser) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomIdAndUserIdAndIsDeletedIsFalse(chatRoomId, loginUser.getId()).orElseThrow(() -> new ChatParticipantNotFoundException("채팅 참여자가 아닙니다."));

       List<ChatMessageListDto> chatMessageListDtoList = chatMessageRepository.findAllByChatRoomIdOrderByCreatedAt(chatRoomId, pageable).stream()
                .map(chatMessage -> {
                    User user = userRepository.findById(chatMessage.getUserId())
                            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
                    return new ChatMessageListDto(chatMessage, user.getNickname());
                })
                .collect(Collectors.toList());

        if (!chatMessageListDtoList.isEmpty()) {
            chatParticipant.updateLastReadMessageId(chatMessageListDtoList.get(chatMessageListDtoList.size() - 1).getId());
        }
        return chatMessageListDtoList;
    }

    @Transactional(readOnly = true)
    public List<ChatRoomListDto> getChatRoomList(User loginUser) {
        List<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByUserIdAndIsDeletedIsFalse(loginUser.getId());
        return chatParticipantList.stream()
                .map(chatParticipant -> {
                    ChatRoom chatRoom = chatParticipant.getChatRoom();
                    Optional<ChatMessage> lastMessageOptional = chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());
                    String lastMessageContent = lastMessageOptional.map(ChatMessage::getContent).orElse("메시지 없음");
                    Instant lastMessageCreatedAt = lastMessageOptional.map(ChatMessage::getCreatedAt).orElse(null);
                    Integer unreadMessageCount = chatParticipant.getLastReadMessageId() != null ? chatMessageRepository.countAllByChatRoomIdAndIdGreaterThan(chatRoom.getId(), new ObjectId(chatParticipant.getLastReadMessageId())) : 0;
                    return new ChatRoomListDto(chatRoom, lastMessageContent, lastMessageCreatedAt, unreadMessageCount);
                }).
                sorted((dto1, dto2) -> {
                    if (dto1.getLastMessageCreatedAt() == null && dto2.getLastMessageCreatedAt() == null) {
                        return 0;
                    } else if (dto1.getLastMessageCreatedAt() == null) {
                        return -1;
                    } else if (dto2.getLastMessageCreatedAt() == null) {
                        return 1;
                    }
                    return dto2.getLastMessageCreatedAt().compareTo(dto1.getLastMessageCreatedAt());
                })
                .toList();
    }

    @Transactional
    public void exitChatRoom(Long chatRoomId, User loginUser) {
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomIdAndUserIdAndIsDeletedIsFalse(chatRoomId, loginUser.getId()).orElseThrow(() -> new ChatParticipantNotFoundException("채팅 참여자가 아닙니다."));
        chatParticipant.exit();
    }

    @Transactional(readOnly = true)
    public Integer getChatMessagePage(Long roomId, Integer size) {
         Integer count = chatMessageRepository.countAllByChatRoomId(roomId);
          return (int) Math.ceil((double) count / size);
    }
}
