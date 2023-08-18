package com.example.honjarang.domain.chat.service;

import com.example.honjarang.domain.DateTimeUtils;
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
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.service.TokenService;
import com.google.firebase.messaging.FirebaseMessaging;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;
    @Mock
    private TokenService tokenService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    @Mock
    private SetOperations<String, Object> setOperations;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private ChatParticipantRepository chatParticipantRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private UserService userService;
    @Mock
    private FirebaseMessaging firebaseMessaging;

    private User user;
    private ChatMessage chatMessage;
    private ChatRoom chatRoom;
    private ChatParticipant chatParticipant;

    private static final String SESSION_USER_PREFIX = "websocket::session::user::";
    private static final String SESSION_CHAT_ROOM_PREFIX = "websocket::session::chatRoom::";
    private static final String CHAT_ROOM_USER_PREFIX = "websocket::chatRoom::user::";

    @BeforeEach
    void setUp() {
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
                .chatRoomId(1L)
                .userId(1L)
                .content("테스트")
                .build();
        chatMessage.setIdForTest(new ObjectId("60f0b0b9e8b9a91c7c7b0b0b"));
        chatMessage.setCreatedAtForTest(DateTimeUtils.parseInstant("2030-01-01 00:00:00"));
        chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatParticipant.setLastReadMessageIdForTest("60f0b0b9e8b9a91c7c7b0b0b");
    }

    @Test
    @DisplayName("채팅방 연결 성공")
    void connectChatRoom_Success() {
        // given
        given(tokenService.getUserByToken("token")).willReturn(user);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(redisTemplate.opsForSet()).willReturn(setOperations);

        // when
        chatService.connectChatRoom(1L, "sessionId", "token");

        // then
    }

    @Test
    @DisplayName("채팅방 연결 해제 성공")
    void disconnectChatRoom_Success() {
        // given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(redisTemplate.opsForSet()).willReturn(setOperations);
        given(valueOperations.get(SESSION_USER_PREFIX + "sessionId")).willReturn("1");
        given(valueOperations.get(SESSION_CHAT_ROOM_PREFIX + "sessionId")).willReturn("1");

        // when
        chatService.disconnectChatRoom("sessionId");

        // then
    }

    @Test
    @DisplayName("채팅 메시지 메시지 큐에 전송 성공")
    void sendChatMessageToQueue_Success() {
        // given
        ChatMessageSendDto chatMessageSendDto = new ChatMessageSendDto("테스트", 1L, "테스트", "test.jpg");

        // when
        chatService.sendChatMessageToQueue(1L, chatMessageSendDto, "sessionId");

        // then
    }

    @Test
    @DisplayName("채팅 메시지 생성 성공")
    void createChatMessage_Success() {
        // given
        ChatMessageCreateDto chatMessageCreateDto = new ChatMessageCreateDto("테스트", 1L, "sessionId", "테스트", "test.jpg");
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(redisTemplate.opsForSet()).willReturn(setOperations);
        given(valueOperations.get(SESSION_USER_PREFIX + "sessionId")).willReturn("1");
        given(setOperations.members(CHAT_ROOM_USER_PREFIX + 1L)).willReturn(Set.of("1"));
        given(chatParticipantRepository.findByChatRoomIdAndUserIdAndIsDeletedIsFalse(1L, 1L)).willReturn(Optional.of(chatParticipant));
        given(chatParticipantRepository.findAllByChatRoomIdAndIsDeletedIsFalse(1L)).willReturn(List.of(chatParticipant));
        given(chatMessageRepository.save(any(ChatMessage.class))).willReturn(chatMessage);
        given(userService.getFcmTokenList(any(User.class))).willReturn(Set.of("fcmToken"));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        chatService.createChatMessage(chatMessageCreateDto);

        // then
    }

    @Test
    @DisplayName("채팅 메시지 목록 조회 성공")
    void getChatMessageList_Success() {
        // given
        Page<ChatMessage> chatMessagePage = new PageImpl<>(List.of(chatMessage));
        given(chatParticipantRepository.findByChatRoomIdAndUserIdAndIsDeletedIsFalse(1L, 1L)).willReturn(Optional.of(chatParticipant));
        given(chatMessageRepository.findAllByChatRoomIdOrderByCreatedAt(eq(1L), any(Pageable.class))).willReturn(chatMessagePage);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        List<ChatMessageListDto> chatMessageList = chatService.getChatMessageList(1L, 1, 10, user);

        // then
        assertThat(chatMessageList).hasSize(1);
        assertThat(chatMessageList.get(0).getId()).isEqualTo("60f0b0b9e8b9a91c7c7b0b0b");
        assertThat(chatMessageList.get(0).getContent()).isEqualTo("테스트");
        assertThat(chatMessageList.get(0).getCreatedAt()).isEqualTo("2030-01-01 00:00:00");
        assertThat(chatMessageList.get(0).getUserId()).isEqualTo(1L);
        assertThat(chatMessageList.get(0).getNickname()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("채팅 메시지 목록 조회 실패 - 채팅 참여자가 아닌 경우")
    void getChatMessageList_ChatParticipantNotFoundException() {
        // given
        given(chatParticipantRepository.findByChatRoomIdAndUserIdAndIsDeletedIsFalse(1L, 1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(ChatParticipantNotFoundException.class, () -> chatService.getChatMessageList(1L, 1, 10, user));
    }

    @Test
    @DisplayName("채팅방 목록 조회 성공")
    void getChatRoomList_Success() {
        // given
        given(chatParticipantRepository.findAllByUserIdAndIsDeletedIsFalse(1L)).willReturn(List.of(chatParticipant));
        given(chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(1L)).willReturn(Optional.of(chatMessage));
        given(chatParticipantRepository.countAllByChatRoomIdAndIsDeletedIsFalse(1L)).willReturn(1);

        // when
        List<ChatRoomListDto> chatRoomList = chatService.getChatRoomList(user);

        // then
        assertThat(chatRoomList).hasSize(1);
        assertThat(chatRoomList.get(0).getId()).isEqualTo(1L);
        assertThat(chatRoomList.get(0).getName()).isEqualTo("테스트 채팅방");
        assertThat(chatRoomList.get(0).getLastMessage()).isEqualTo("테스트");
        assertThat(chatRoomList.get(0).getLastMessageCreatedAt()).isEqualTo("2030-01-01 00:00:00");
        assertThat(chatRoomList.get(0).getUnreadMessageCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("채팅방 퇴장 성공")
    void exitChatRoom_Success() {
        // given
        given(chatParticipantRepository.findByChatRoomIdAndUserIdAndIsDeletedIsFalse(1L, 1L)).willReturn(Optional.of(chatParticipant));

        // when
        chatService.exitChatRoom(1L, user);

        // then
    }

    @Test
    @DisplayName("채팅방 퇴장 실패 - 채팅 참여자가 아닌 경우")
    void exitChatRoom_ChatParticipantNotFoundException() {
        // given
        given(chatParticipantRepository.findByChatRoomIdAndUserIdAndIsDeletedIsFalse(1L, 1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(ChatParticipantNotFoundException.class, () -> chatService.exitChatRoom(1L, user));
    }

    @Test
    @DisplayName("채팅 메시지 페이지 수 조회 성공")
    void getChatMessagePage_Success() {
        // given
        given(chatMessageRepository.countAllByChatRoomId(1L)).willReturn(1);

        // when
        Integer page = chatService.getChatMessagePageCount(1L, 10);

        // then
        assertThat(page).isEqualTo(1);
    }

    @Test
    @DisplayName("1:1 채팅방 생성 성공")
    void createOneToOneChatRoom_Success() {
        // given
        User target = User.builder().build();
        target.setIdForTest(2L);

        given(userRepository.findById(2L)).willReturn(Optional.of(target));
        given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);

        // when
        Long id = chatService.createOneToOneChatRoom(user, 2L);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("1:1 채팅방 생성 실패 - 상대방이 존재하지 않는 경우")
    void createOneToOneChatRoom_UserNotFoundException() {
        // given
        given(userRepository.findById(2L)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> chatService.createOneToOneChatRoom(user, 2L));
    }
}