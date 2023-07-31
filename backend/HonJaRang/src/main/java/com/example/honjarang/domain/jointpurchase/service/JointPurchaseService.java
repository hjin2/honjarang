package com.example.honjarang.domain.jointpurchase.service;

import com.example.honjarang.domain.jointpurchase.dto.JointPurchaseCreateDto;
import com.example.honjarang.domain.jointpurchase.dto.PlaceDto;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.jointpurchase.exception.ProductNotFoundException;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseRepository;
import com.example.honjarang.domain.map.exception.PlaceNotFoundException;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

@RequiredArgsConstructor
@Service
public class JointPurchaseService {

    private final JointPurchaseRepository jointPurchaseRepository;
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
            if(jsonNode.get("items").size() == 0) {
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
            if(jsonNode.get("documents").size() == 0) {
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
}
