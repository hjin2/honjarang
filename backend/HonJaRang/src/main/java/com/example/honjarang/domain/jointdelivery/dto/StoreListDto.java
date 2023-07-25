package com.example.honjarang.domain.jointdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreListDto {
    private Long id;
    private String name;
    private String image;
    private String address;
}
