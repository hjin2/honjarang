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

    @Test
    @DisplayName("키워드로 좌표 조회 성공")
    void coordinates_Success() throws Exception {
        // given
        given(mapService.getCoordinate("서울특별시 강남구")).willReturn(new CoordinateDto(37.123456, 127.123456));

        // when & then
        mockMvc.perform(get("/api/v1/maps/coordinates")
                        .param("keyword", "서울특별시 강남구"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(37.123456))
                .andExpect(jsonPath("$.longitude").value(127.123456));
    }

    @Test
    @DisplayName("키워드로 좌표 조회 실패 - 잣소가 존재하지 않는 경우")
    void coordinates_LocationNotFoundException() throws Exception {
        // given
        given(mapService.getCoordinate("서울특별시 강남구")).willThrow(new LocationNotFoundException(""));

        // when & then
        mockMvc.perform(get("/api/v1/maps/coordinates")
                        .param("keyword", "서울특별시 강남구"))
                .andExpect(status().isNotFound());
    }
}