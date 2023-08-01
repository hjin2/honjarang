package com.example.honjarang.domain.jointpurchase.service;

import com.example.honjarang.domain.jointpurchase.dto.*;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchaseApplicant;
import com.example.honjarang.domain.jointpurchase.exception.JointPurchaseCanceledException;
import com.example.honjarang.domain.jointpurchase.exception.JointPurchaseNotFoundException;
import com.example.honjarang.domain.jointpurchase.exception.ProductNotFoundException;
import com.example.honjarang.domain.jointpurchase.exception.UnauthorizedJointPurchaseAccessException;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseApplicantRepository;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseRepository;
import com.example.honjarang.domain.map.exception.PlaceNotFoundException;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import java.util.List;

@RequiredArgsConstructor
@Service
public class JointPurchaseService {

    private final JointPurchaseRepository jointPurchaseRepository;
    private final JointPurchaseApplicantRepository jointPurchaseApplicantRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.client-secret}")
    private String naverClientSecret;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    @Transactional
    public void createJointPurchase(JointPurchaseCreateDto jointPurchaseCreateDto, User loginUser) {
        String productImage = getProductImage(jointPurchaseCreateDto.getProductName());
        PlaceDto placeDto = getPlace(jointPurchaseCreateDto.getPlaceKeyword());
        JointPurchase jointPurchase = jointPurchaseCreateDto.toEntity(jointPurchaseCreateDto, loginUser, productImage, placeDto);
        jointPurchaseRepository.save(jointPurchase);
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

    private PlaceDto getPlace(String keyword) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        URI targetUrl = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("query", keyword)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        ResponseEntity<String> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            if (jsonNode.get("documents").size() == 0) {
                throw new PlaceNotFoundException("장소를 찾을 수 없습니다.");
            }
            String placeName = jsonNode.get("documents").get(0).get("place_name").asText();
            Double latitude = jsonNode.get("documents").get(0).get("y").asDouble();
            Double longitude = jsonNode.get("documents").get(0).get("x").asDouble();
            return new PlaceDto(placeName, latitude, longitude);
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
    public List<JointPurchaseListDto> getJointPurchaseList(Integer page, Integer size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<JointPurchase> jointPurchases = jointPurchaseRepository.findAllByDeadlineAfterAndIsCanceledFalse(LocalDateTime.now(), pageable);
        return jointPurchases.getContent().stream()
                .filter(jointPurchase -> {
                    Integer currentPersonCount = jointPurchaseApplicantRepository.countByJointPurchaseId(jointPurchase.getId());
                    return currentPersonCount < jointPurchase.getTargetPersonCount();
                })
                .map(jointPurchase -> new JointPurchaseListDto(jointPurchase, jointPurchaseApplicantRepository.countByJointPurchaseId(jointPurchase.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public JointPurchaseDto getJointPurchase(Long jointPurchaseId) {
        JointPurchase jointPurchase = jointPurchaseRepository.findById(jointPurchaseId).orElseThrow(() -> new JointPurchaseNotFoundException("공동구매를 찾을 수 없습니다."));
        Integer currentPersonCount = jointPurchaseApplicantRepository.countByJointPurchaseId(jointPurchaseId);
        return new JointPurchaseDto(jointPurchase, currentPersonCount);
    }

    @Transactional(readOnly = true)
    public List<JointPurchaseApplicantListDto> getJointPurchaseApplicantList(Long jointPurchaseId) {
        return jointPurchaseApplicantRepository.findAllByJointPurchaseId(jointPurchaseId).stream()
                .map(JointPurchaseApplicantListDto::new)
                .toList();
    }
}