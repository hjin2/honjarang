package com.example.honjarang.domain.jointpurchase.service;

import com.example.honjarang.domain.chat.entity.ChatParticipant;
import com.example.honjarang.domain.chat.entity.ChatRoom;
import com.example.honjarang.domain.chat.exception.ChatParticipantNotFoundException;
import com.example.honjarang.domain.chat.exception.ChatRoomNotFoundException;
import com.example.honjarang.domain.chat.repository.ChatParticipantRepository;
import com.example.honjarang.domain.chat.repository.ChatRoomRepository;
import com.example.honjarang.domain.jointpurchase.dto.*;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchaseApplicant;
import com.example.honjarang.domain.jointpurchase.exception.*;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseApplicantRepository;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseRepository;
import com.example.honjarang.domain.map.dto.CoordinateDto;
import com.example.honjarang.domain.map.exception.PlaceNotFoundException;
import com.example.honjarang.domain.map.service.MapService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.InsufficientPointsException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JointPurchaseService {

    private final JointPurchaseRepository jointPurchaseRepository;
    private final JointPurchaseApplicantRepository jointPurchaseApplicantRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MapService mapService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.client-secret}")
    private String naverClientSecret;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    @Transactional
    public Long createJointPurchase(JointPurchaseCreateDto jointPurchaseCreateDto, User loginUser) {
        String productImage = getProductImage(jointPurchaseCreateDto.getProductName());
        JointPurchase jointPurchase = jointPurchaseRepository.save(jointPurchaseCreateDto.toEntity(loginUser, productImage));
        ChatRoom chatRoom = ChatRoom.builder()
                .name(jointPurchase.getId().toString() + "번 공동구매 채팅방")
                .build();
        chatRoomRepository.save(chatRoom);
        chatParticipantRepository.save(ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(loginUser)
                .build());
        return jointPurchaseRepository.save(jointPurchase).getId();
    }

    private String getProductImage(String productName) {
        String url = "https://openapi.naver.com/v1/search/shop.json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverClientId);
        headers.set("X-Naver-Client-Secret", naverClientSecret);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("query", productName)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        ResponseEntity<String> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            if (jsonNode.get("items").size() == 0) {
                throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
            }
            return jsonNode.get("items").get(0).get("image").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void cancelJointPurchase(Long jointPurchaseId, User loginUser) {
        JointPurchase jointPurchase = jointPurchaseRepository.findById(jointPurchaseId).orElseThrow(() -> new JointPurchaseNotFoundException("공동구매를 찾을 수 없습니다."));

        if (!jointPurchase.getUser().getId().equals(loginUser.getId())) {
            throw new UnauthorizedJointPurchaseAccessException("공동구매를 취소할 권한이 없습니다.");
        }
        if (jointPurchase.getIsCanceled()) {
            throw new JointPurchaseCanceledException("이미 취소된 공동구매입니다.");
        }
        if (jointPurchase.getDeadline().isAfter(LocalDateTime.now())) {
            jointPurchase.getUser().addPoint(1000);
        }

        List<JointPurchaseApplicant> jointPurchaseApplicants = jointPurchaseApplicantRepository.findAllByJointPurchaseId(jointPurchaseId);
        for (JointPurchaseApplicant jointPurchaseApplicant : jointPurchaseApplicants) {
            jointPurchaseApplicant.getUser().addPoint(jointPurchase.getPrice() * jointPurchaseApplicant.getQuantity());
        }
        jointPurchase.cancel();
    }

    @Transactional(readOnly = true)
    public List<JointPurchaseListDto> getJointPurchaseList(Integer page, Integer size, String keyword, User loginUser) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        List<JointPurchase> jointPurchases = jointPurchaseRepository.findAllByIsCanceledFalseAndDeadlineAfterAndDistanceLessThanAndTargetPersonCountGreaterThanOrderByCreatedAtDesc(LocalDateTime.now(), loginUser.getLatitude(), loginUser.getLongitude(), keyword, pageable).toList();
        List<JointPurchaseListDto> jointPurchaseListDtoList = new ArrayList<>();
        for(JointPurchase jointPurchase : jointPurchases) {
            Integer currentPersonCount = jointPurchaseApplicantRepository.countByJointPurchaseId(jointPurchase.getId());
            jointPurchaseListDtoList.add(new JointPurchaseListDto(jointPurchase, currentPersonCount));
        }
        return jointPurchaseListDtoList;
    }

    @Transactional(readOnly = true)
    public JointPurchaseDto getJointPurchase(Long jointPurchaseId, User loginUser) {
        JointPurchase jointPurchase = jointPurchaseRepository.findById(jointPurchaseId).orElseThrow(() -> new JointPurchaseNotFoundException("공동구매를 찾을 수 없습니다."));
        Integer currentPersonCount = jointPurchaseApplicantRepository.countByJointPurchaseId(jointPurchaseId);
        return new JointPurchaseDto(jointPurchase, currentPersonCount, loginUser.getPoint());
    }

    @Transactional(readOnly = true)
    public List<JointPurchaseApplicantListDto> getJointPurchaseApplicantList(Long jointPurchaseId) {
        List<JointPurchaseApplicant> jointPurchaseApplicants = jointPurchaseApplicantRepository.findAllByJointPurchaseId(jointPurchaseId);
        List<JointPurchaseApplicantListDto> jointPurchaseApplicantListDtoList = new ArrayList<>();
        for(JointPurchaseApplicant jointPurchaseApplicant : jointPurchaseApplicants) {
            jointPurchaseApplicantListDtoList.add(new JointPurchaseApplicantListDto(jointPurchaseApplicant));
        }
        return jointPurchaseApplicantListDtoList;
    }

    @Transactional
    public void applyJointPurchase(JointPurchaseApplyDto jointPurchaseApplyDto, User loginUser) {
        JointPurchase jointPurchase = jointPurchaseRepository.findById(jointPurchaseApplyDto.getJointPurchaseId()).orElseThrow(() -> new JointPurchaseNotFoundException("공동구매를 찾을 수 없습니다."));

        if (jointPurchase.getIsCanceled()) {
            throw new JointPurchaseCanceledException("이미 취소된 공동구매입니다.");
        }
        if (jointPurchase.getDeadline().isBefore(LocalDateTime.now())) {
            throw new JointPurchaseExpiredException("공동구매가 마감되었습니다.");
        }
        if (jointPurchaseApplicantRepository.existsByJointPurchaseIdAndUserId(jointPurchaseApplyDto.getJointPurchaseId(), loginUser.getId())) {
            throw new JointPurchaseAlreadyAppliedException("이미 신청한 공동구매입니다.");
        }
        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        if(user.getPoint() < jointPurchase.getPrice() * jointPurchaseApplyDto.getQuantity() + jointPurchase.getDeliveryCharge() / jointPurchase.getTargetPersonCount()) {
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }
        user.subtractPoint(jointPurchase.getPrice() * jointPurchaseApplyDto.getQuantity());
        user.subtractPoint(jointPurchase.getDeliveryCharge() / jointPurchase.getTargetPersonCount());

        JointPurchaseApplicant jointPurchaseApplicant = JointPurchaseApplicant.builder()
                .jointPurchase(jointPurchase)
                .user(user)
                .quantity(jointPurchaseApplyDto.getQuantity())
                .build();
        jointPurchaseApplicantRepository.save(jointPurchaseApplicant);

        ChatRoom chatRoom = chatRoomRepository.findByName(jointPurchase.getId().toString() + "번 공동구매 채팅방").orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomIdAndUserId(chatRoom.getId(), user.getId()).orElse(null);
        if(chatParticipant == null) {
            chatParticipantRepository.save(ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .user(user)
                    .build());
        } else {
            chatParticipant.reEnter();
        }
    }

    @Transactional
    public void cancelJointPurchaseApplicant(Long jointPurchaseId, User loginUser) {
        JointPurchaseApplicant jointPurchaseApplicant = jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(jointPurchaseId, loginUser.getId()).orElseThrow(() -> new JointPurchaseApplicantNotFoundException("공동구매 신청자를 찾을 수 없습니다."));

        if (jointPurchaseApplicant.getJointPurchase().getIsCanceled()) {
            throw new JointPurchaseCanceledException("이미 취소된 공동구매입니다.");
        }
        if (jointPurchaseApplicant.getJointPurchase().getDeadline().isBefore(LocalDateTime.now())) {
            throw new JointPurchaseExpiredException("공동구매가 마감되었습니다.");
        }

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        user.addPoint(jointPurchaseApplicant.getJointPurchase().getPrice() * jointPurchaseApplicant.getQuantity());

        jointPurchaseApplicantRepository.delete(jointPurchaseApplicant);

        ChatRoom chatRoom = chatRoomRepository.findByName(jointPurchaseId.toString() + "번 공동구매 채팅방").orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomIdAndUserId(chatRoom.getId(), user.getId()).orElseThrow(() -> new ChatParticipantNotFoundException("채팅방 참가자를 찾을 수 없습니다."));
        chatParticipant.exit();
    }

    @Transactional
    public void confirmReceived(Long jointPurchaseId, User loginUser) {
        JointPurchaseApplicant jointPurchaseApplicant = jointPurchaseApplicantRepository.findByJointPurchaseIdAndUserId(jointPurchaseId, loginUser.getId()).orElseThrow(() -> new JointPurchaseApplicantNotFoundException("공동구매 신청자를 찾을 수 없습니다."));

        if (jointPurchaseApplicant.getJointPurchase().getIsCanceled()) {
            throw new JointPurchaseCanceledException("이미 취소된 공동구매입니다.");
        }
        if (jointPurchaseApplicant.getJointPurchase().getDeadline().isAfter(LocalDateTime.now())) {
            throw new JointPurchaseNotClosedException("공동구매가 마감되지 않았습니다.");
        }
        if(jointPurchaseApplicant.getIsReceived()) {
            throw new JointPurchaseAlreadyReceivedException("이미 수령확인한 공동구매입니다.");
        }

        jointPurchaseApplicant.confirmReceived();

        jointPurchaseApplicant.getJointPurchase().getUser().addPoint(jointPurchaseApplicant.getJointPurchase().getPrice() * jointPurchaseApplicant.getQuantity());
        jointPurchaseApplicant.getJointPurchase().getUser().addPoint(jointPurchaseApplicant.getJointPurchase().getDeliveryCharge() / jointPurchaseApplicant.getJointPurchase().getTargetPersonCount());
    }

    @Transactional(readOnly = true)
    public Integer getJointPurchasePageCount(Integer size) {
        Integer jointPurchaseCount = jointPurchaseRepository.countByIsCanceledFalseAndDeadlineAfter(LocalDateTime.now());
        return (int) Math.ceil((double) jointPurchaseCount / size);
    }
}
