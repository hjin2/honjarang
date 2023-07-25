package com.example.honjarang.domain.jointdelivery.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreListDto {
    private Long id;
    private String name;
    private String image;
    private String address;
}
