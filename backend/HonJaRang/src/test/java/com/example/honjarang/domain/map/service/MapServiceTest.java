package com.example.honjarang.domain.map.service;

import com.example.honjarang.domain.map.dto.CoordinateDto;
import com.example.honjarang.domain.map.exception.LocationNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MapServiceTest {

    @InjectMocks
    private MapService mapService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    @Test
    @DisplayName("키워드로 좌표 조회 성공")
    void getCoordinate_Success() throws JsonProcessingException {
        // given
        String responseBody = """
                {
                    "documents": [
                        {
                            "x": 127.123456,
                            "y": 37.123456
                        }
                    ]
                }""";

        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        JsonNode jsonNode = new ObjectMapper().readTree(responseEntity.getBody());

        given(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).willReturn(responseEntity);
        given(objectMapper.readTree(eq(responseEntity.getBody()))).willReturn(jsonNode);

        // when
        CoordinateDto coordinateDto = mapService.getCoordinate("서울특별시 강남구");

        // then
        assertThat(coordinateDto.getLatitude()).isEqualTo(37.123456);
        assertThat(coordinateDto.getLongitude()).isEqualTo(127.123456);
    }

    @Test
    @DisplayName("키워드로 좌표 조회 실패 - 장소를 찾을 수 없는 경우")
    void getCoordinate_LocationNotFoundException() throws JsonProcessingException {
        // given
        String responseBody = """
                {
                    "documents": []
                }""";

        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        JsonNode jsonNode = new ObjectMapper().readTree(responseEntity.getBody());

        given(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).willReturn(responseEntity);
        given(objectMapper.readTree(eq(responseEntity.getBody()))).willReturn(jsonNode);

        // when & then
        assertThrows(LocationNotFoundException.class, () -> mapService.getCoordinate("서울특별시 강남구"));
    }
}