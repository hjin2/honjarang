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

        VideoChatRoom check = videoChatRoomRepository.findBySessionId((String)params.get("customSessionId"));

        if (check == null) {
            Category option = Category.FREE;
            switch((String)params.get("category")) {
                case "FREE": option = Category.FREE; break;
                case "MUKBANG": option = Category.MUKBANG; break;
                case "GAME": option = Category.GAME; break;
                case "STUDY": option = Category.STUDY; break;
            }

            VideoChatRoom videoChatRoom = VideoChatRoom.builder()
                    .sessionId(properties.customSessionId())
                    .category(option)
                    .title((String)params.get("title"))
                    .onlyVoice((Boolean)params.get("onlyVoice"))
                    .build();
            videoChatRoomRepository.save(videoChatRoom);
        }
        Session session = openvidu.createSession(properties);
        return session.getSessionId();
    }

    @Transactional
    public String createConnection(String sessionId, Map<String, Object> params) {

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
