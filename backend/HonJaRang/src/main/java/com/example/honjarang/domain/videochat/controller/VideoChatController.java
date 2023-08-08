package com.example.honjarang.domain.videochat.controller;

import javax.annotation.PostConstruct;

import com.example.honjarang.domain.videochat.service.VideoChatService;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video-room")
public class VideoChatController {

    private final VideoChatService videoChatService;

    // 화상 채팅 방을 만드는 메소드
    @PostMapping("/sessions")
    public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        return new ResponseEntity<>(videoChatService.initializeSession(params), HttpStatus.OK);
    }

    // 화상 채팅 방에 접속하는 메소드
    @PostMapping("/sessions/{sessionId}/connections")
    public ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId,
                                                   @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        return new ResponseEntity<>(videoChatService.createConnection(sessionId, params),HttpStatus.OK);
    }

}
