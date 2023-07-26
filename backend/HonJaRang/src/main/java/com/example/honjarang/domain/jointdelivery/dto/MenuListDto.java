package com.example.honjarang.domain.jointdelivery.dto;

import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MenuListDto {
    private String id;
    private String name;
    private Integer price;
    private String image;
    private Long storeId;

    public MenuListDto(Menu menu) {
        this.id = menu.getId().toString();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.image = menu.getImage();
        this.storeId = menu.getStoreId();
    }
}
