package com.example.honjarang.domain.chat.service;

import com.example.honjarang.domain.chat.document.ChatMessage;
import com.example.honjarang.domain.chat.dto.ChatMessageCreateDto;
import com.example.honjarang.domain.chat.dto.ChatMessageListDto;
import com.example.honjarang.domain.chat.dto.ChatMessageSendDto;
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
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.service.TokenService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final FirebaseMessaging firebaseMessaging;
    private final UserService userService;

    private static final String SESSION_USER_PREFIX = "websocket::session::user::";
    private static final String SESSION_CHAT_ROOM_PREFIX = "websocket::session::chatRoom::";
    private static final String CHAT_ROOM_USER_PREFIX = "websocket::chatRoom::user::";

    public void connectChatRoom(Long roomId, String sessionId, String token) {
        User user = tokenService.getUserByToken(token);
        redisTemplate.opsForValue().set(SESSION_USER_PREFIX + sessionId, user.getId());
        redisTemplate.opsForValue().set(SESSION_CHAT_ROOM_PREFIX + sessionId, roomId);
        redisTemplate.opsForSet().add(CHAT_ROOM_USER_PREFIX + roomId, user.getId());
    }

    public void disconnectChatRoom(String sessionId) {
        Long userId = Long.parseLong((String) redisTemplate.opsForValue().get(SESSION_USER_PREFIX + sessionId));
        redisTemplate.delete(SESSION_USER_PREFIX + sessionId);
        Long roomId = Long.parseLong((String) redisTemplate.opsForValue().get(SESSION_CHAT_ROOM_PREFIX + sessionId));
        redisTemplate.delete(SESSION_CHAT_ROOM_PREFIX + sessionId);
        redisTemplate.opsForSet().remove(CHAT_ROOM_USER_PREFIX + roomId, userId);
    }

    public void sendChatMessageToQueue(Long roomId, ChatMessageSendDto chatMessageSendDto, String sessionId) {
        ChatMessageCreateDto chatMessageCreateDto = new ChatMessageCreateDto(chatMessageSendDto.getContent(), roomId, sessionId);
        rabbitTemplate.convertAndSend("amq.topic", "room." + roomId, chatMessageCreateDto);
    }

    @Transactional
    public void createChatMessage(ChatMessageCreateDto chatMessageCreateDto) {
        Long userId = Long.parseLong((String) Objects.requireNonNull(redisTemplate.opsForValue().get(SESSION_USER_PREFIX + chatMessageCreateDto.getSessionId())));
        ChatMessage chatMessage = chatMessageCreateDto.toEntity(userId);
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        // 채팅방 실시간 참여자들의 마지막 읽은 메시지를 업데이트
        Set<Object> connectedUserId = redisTemplate.opsForSet().members(CHAT_ROOM_USER_PREFIX + chatMessage.getChatRoomId());
        Objects.requireNonNull(connectedUserId)
                .forEach(id -> {
                    Optional<ChatParticipant> chatParticipantOptional = chatParticipantRepository.findByChatRoomIdAndUserIdAndIsDeletedIsFalse(chatMessage.getChatRoomId(), Long.parseLong((String) id));
                    chatParticipantOptional.ifPresent(chatParticipant -> chatParticipant.updateLastReadMessageId(savedChatMessage.getId().toString()));
                });

        // 채팅방 참여자들에게 푸쉬 알림 전송
        List<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByChatRoomIdAndIsDeletedIsFalse(chatMessage.getChatRoomId());
        for (ChatParticipant chatParticipant : chatParticipantList) {
            Set<String> fcmTokenSet = userService.getFcmTokenList(chatParticipant.getUser());
            if (!fcmTokenSet.isEmpty()) {
                User user = userRepository.findById(chatMessage.getUserId())
                        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
                String title = user.getNickname();
                String body = chatMessage.getContent();
                fcmTokenSet.forEach(token -> sendPushNotification(token, title, body));
            }
        }
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

        // 마지막 읽은 메시지를 업데이트
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

    private void sendPushNotification(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();
        try {
            String response = firebaseMessaging.send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
