package com.example.honjarang.domain.videochat.service;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.videochat.dto.VideoChatListDto;
import com.example.honjarang.domain.videochat.entity.Category;
import com.example.honjarang.domain.videochat.entity.VideoChatParticipant;
import com.example.honjarang.domain.videochat.entity.VideoChatRoom;
import com.example.honjarang.domain.videochat.exception.ExistVideoChatException;
import com.example.honjarang.domain.videochat.repository.VideoChatParticipantRepository;
import com.example.honjarang.domain.videochat.repository.VideoChatRoomRepository;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class VideoChatService {

    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    private final VideoChatRoomRepository videoChatRoomRepository;

    private final VideoChatParticipantRepository videoChatParticipantRepository;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @Transactional
    public String initializeSession(Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {

        SessionProperties properties = SessionProperties.fromJson(params).build();

        if (videoChatRoomRepository.findBySessionId(properties.customSessionId()) != null)
            throw new ExistVideoChatException("동일한 방이 이미 있습니다.");

        Session session = openvidu.createSession(properties);
        VideoChatRoom videoChatRoom = VideoChatRoom.builder()
                .sessionId(properties.customSessionId())
                .category(Category.MUKBANG)
                .isScreen(properties.defaultRecordingProperties().hasVideo())
                .build();
        videoChatRoomRepository.save(videoChatRoom);
        return session.getSessionId();
    }

    @Transactional
    public String createConnection(String sessionId, Map<String, Object> params, User user) {

        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            return "HttpStatus.NOT_FOUND";
        }
        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
        Connection connection = null;

        try {
            connection = session.createConnection(properties);
        } catch (OpenViduJavaClientException e) {
            throw new RuntimeException(e);
        } catch (OpenViduHttpException e) {
            throw new RuntimeException(e);
        }

        VideoChatParticipant videoChatParticipant = VideoChatParticipant.builder()
                        .videoChatRoom(videoChatRoomRepository.findBySessionId(sessionId))
                        .user(user)
                        .build();

        videoChatParticipantRepository.save(videoChatParticipant);
        return connection.getToken();
    }

    @Transactional
    public void closeConnection(String sessionId, Long userId)
        throws OpenViduJavaClientException, OpenViduHttpException {
        VideoChatRoom videoChatRoom = videoChatRoomRepository.findBySessionId(sessionId);
        videoChatParticipantRepository.deleteByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<VideoChatListDto> getSessionList() {

        List<VideoChatRoom> videoChatRooms = videoChatRoomRepository.findAll();
        List<VideoChatListDto> videoChatRoomList = new ArrayList<>();
        for(VideoChatRoom videoChatRoom  : videoChatRooms) {
            Integer count = videoChatParticipantRepository.countByVideoChatRoom(videoChatRoom);
            if (count >= 8) {
                continue;
            }
            videoChatRoomList.add(new VideoChatListDto(videoChatRoom, count));
        }
        return videoChatRoomList;
    }


}
