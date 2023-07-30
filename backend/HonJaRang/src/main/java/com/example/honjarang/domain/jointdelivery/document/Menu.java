package com.example.honjarang.domain.jointdelivery.document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document
public class Menu {
    @Id
    private ObjectId id;
    private String name;
    private Integer price;
    private String image;
    private Long storeId;

    @Builder
    public Menu(String name, Integer price, String image, Long storeId) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.storeId = storeId;
    }

    public void setIdForTest(ObjectId id) {
        this.id = id;
    }
}