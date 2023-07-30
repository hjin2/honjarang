package com.example.honjarang.domain.jointpurchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceDto {
    private String placeName;
    private Double latitude;
    private Double longitude;
}
