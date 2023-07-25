package com.example.honjarang.domain.map.controller;

import com.example.honjarang.domain.map.dto.CoordinateDto;
import com.example.honjarang.domain.map.exception.LocationNotFoundException;
import com.example.honjarang.domain.map.service.MapService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MapController.class)
class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MapService mapService;

    @MockBean
    private MongoTemplate mongoTemplate;

    private static final String TEST_KEYWORD = "멀티캠퍼스";

    @Test
    @DisplayName("키워드로 좌표 조회 성공")
    void coordinates_Success() throws Exception{
        // given
        given(mapService.getCoordinate(TEST_KEYWORD)).willReturn(new CoordinateDto(127.039604663862, 37.5012860931305));

        // when & then
        mockMvc.perform(get("/api/v1/maps/coordinates")
                        .param("keyword", TEST_KEYWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(127.039604663862))
                .andExpect(jsonPath("$.longitude").value(37.5012860931305));
    }

    @Test
    @DisplayName("키워드로 좌표 조회 실패 - 잣소가 존재하지 않는 경우")
    void coordinates_LocationNotFoundException() {
        // given
        given(mapService.getCoordinate(TEST_KEYWORD)).willThrow(new LocationNotFoundException(""));

        // when & then
        assertThrows(LocationNotFoundException.class, () -> mapService.getCoordinate(TEST_KEYWORD));
    }
}