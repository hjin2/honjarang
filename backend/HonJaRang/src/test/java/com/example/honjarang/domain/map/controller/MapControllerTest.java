package com.example.honjarang.domain.map.controller;

import com.example.honjarang.domain.map.dto.CoordinateDto;
import com.example.honjarang.domain.map.exception.LocationNotFoundException;
import com.example.honjarang.domain.map.service.MapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MapController.class)
class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MapService mapService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

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
                .andExpect(jsonPath("$.longitude").value(127.123456))
                .andDo(document("maps/coordinates",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("keyword").description("검색할 키워드")
                        ),
                        responseFields(
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")
                        )
                ));
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