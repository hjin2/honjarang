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
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    private final S3Client s3Client;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @Transactional
    public String initializeSession(Map<String, Object> params, MultipartFile multipartFile)
            throws OpenViduJavaClientException, OpenViduHttpException, IOException {

        SessionProperties properties = SessionProperties.fromJson(params).build();

        VideoChatRoom check = videoChatRoomRepository.findBySessionId((String)params.get("customSessionId"));

        if (check == null) {
            Category option = Category.FREE;
            switch((String)params.get("category")) {
                case "FREE": option = Category.FREE; break;
                case "MUKBANG": option = Category.MUKBANG; break;
                case "GAME": option = Category.GAME; break;
                case "HELP": option = Category.HELP; break;
            }

            String thumbnail = "";
            if(multipartFile==null) {
                switch (option) {
                    case FREE:
                        thumbnail = "https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/free.png";
                        break;
                    case MUKBANG:
                        thumbnail = "https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/mukbang.png";
                        break;
                    case GAME:
                        thumbnail = "https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/game.png";
                        break;
                    case HELP:
                        thumbnail = "https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/help.png";
                        break;
                }
            }else{
                // 사진 s3에 저장하기
                String uuid = UUID.randomUUID().toString();
                s3Client.putObject(PutObjectRequest.builder()
                        .bucket("honjarang-bucket")
                        .key("thumbnail/" + uuid + multipartFile.getOriginalFilename())
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .contentType(multipartFile.getContentType())
                        .build(), RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

                // thumbnail에 풀주소 저장하기
                thumbnail = "https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/" + uuid + multipartFile.getOriginalFilename();

            }

            VideoChatRoom videoChatRoom = VideoChatRoom.builder()
                    .sessionId(properties.customSessionId())
                    .category(option)
                    .title((String)params.get("title"))
                    .onlyVoice((Boolean)params.get("onlyVoice"))
                    .thumbnail(thumbnail)
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
    public List<VideoChatListDto> getSessionList(String category) {

        Category option = Category.FREE;

        switch(category) {
            case "free": option = Category.FREE; break;
            case "game": option = Category.GAME; break;
            case "study": option = Category.STUDY; break;
            case "mukbang": option = Category.MUKBANG; break;
            case "help": option = Category.HELP; break;
            case "honsul": option = Category.HONSUL; break;
        }


        List<VideoChatRoom> videoChatRooms = videoChatRoomRepository.findByCategory(option);
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
