package com.example.honjarang.domain.map.controller;

import com.example.honjarang.domain.map.dto.CoordinateDto;
import com.example.honjarang.domain.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/maps")
public class MapController {

    private final MapService mapService;

    @GetMapping("/coordinates")
    public ResponseEntity<CoordinateDto> coordinates(String keyword) {
        CoordinateDto coordinateDto = mapService.getCoordinate(keyword);
        return ResponseEntity.ok(coordinateDto);
    }
}
