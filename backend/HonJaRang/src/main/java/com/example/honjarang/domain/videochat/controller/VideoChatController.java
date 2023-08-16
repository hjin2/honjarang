package com.example.honjarang.domain.videochat.controller;

import javax.annotation.PostConstruct;

import com.example.honjarang.domain.post.dto.PostCreateDto;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.videochat.dto.VideoChatListDto;
import com.example.honjarang.domain.videochat.dto.VideoChatRoomCreateDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video-room/sessions")
public class VideoChatController {

    private final VideoChatService videoChatService;

    // 화상 채팅 방 생성
    @PostMapping("")
    public ResponseEntity<String> initializeSession(@ModelAttribute(name = "params")VideoChatRoomCreateDto params, @RequestPart(value = "thumbnail_image", required = false) MultipartFile thumbnailImage)
            throws OpenViduJavaClientException, OpenViduHttpException, IOException {
        return new ResponseEntity<>(videoChatService.initializeSession(params, thumbnailImage), HttpStatus.OK);
    }

    // 화상 채팅 방 접속
    @PostMapping("/{sessionId}/connections")
    public ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId,
                                                   @RequestBody(required = false) Map<String, Object> params, @CurrentUser User user) {
        return new ResponseEntity<>(videoChatService.createConnection(sessionId, params, user),HttpStatus.OK);
    }

    // 화상 채팅 방 퇴장
    @DeleteMapping("/{userId}/connections")
    public ResponseEntity<Long> closeConnection(@PathVariable("userId") Long userId)
            throws OpenViduJavaClientException, OpenViduHttpException {
        videoChatService.closeConnection(userId);
        return ResponseEntity.ok().build();
    }

    // 화상 채팅 방 목록 조회
    @GetMapping("/{category}")
    public List<VideoChatListDto> getSessionList(@PathVariable("category") String category, @RequestParam(value="page", defaultValue = "1") int page) {
        return videoChatService.getSessionList(category, page);
    }

    @GetMapping("/{category}/page")
    public ResponseEntity<Integer> getVideoChatRoomPageCount(@PathVariable("category") String category, @RequestParam Integer size) {
        return ResponseEntity.ok(videoChatService.getChatRoomPageCount(category, size));
    }
}
