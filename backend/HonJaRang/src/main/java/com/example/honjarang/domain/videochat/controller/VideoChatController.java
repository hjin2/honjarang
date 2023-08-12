package com.example.honjarang.domain.videochat.controller;

import javax.annotation.PostConstruct;

import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.videochat.dto.VideoChatListDto;
import com.example.honjarang.domain.videochat.entity.Category;
import com.example.honjarang.domain.videochat.entity.VideoChatParticipant;
import com.example.honjarang.domain.videochat.entity.VideoChatRoom;
import com.example.honjarang.domain.videochat.service.VideoChatService;
import com.example.honjarang.security.CurrentUser;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video-room/sessions")
public class VideoChatController {

    private final VideoChatService videoChatService;

    // 화상 채팅 방 생성
    @PostMapping("")
    public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        return new ResponseEntity<>(videoChatService.initializeSession(params), HttpStatus.OK);
    }

    // 화상 채팅 방 접속
    @PostMapping("/{sessionId}/connections")
    public ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId,
                                                   @RequestBody(required = false) Map<String, Object> params) {
        return new ResponseEntity<>(videoChatService.createConnection(sessionId, params),HttpStatus.OK);
    }

    // 화상 채팅 방 퇴장
    @DeleteMapping("/{sessionId}/connections")
    public ResponseEntity<Long> closeConnection(@PathVariable("sessionId") String sessionId,
                                                @RequestBody Map<String, Long> user) throws OpenViduJavaClientException, OpenViduHttpException {
        videoChatService.closeConnection(sessionId, user.get("user_id"));
        return ResponseEntity.ok().build();
    }

    // 화상 채팅 방 목록 조회
    @GetMapping("")
    public List<VideoChatListDto> getSessionList() {
        return videoChatService.getSessionList();
    }


}
