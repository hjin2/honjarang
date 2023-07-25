package com.example.honjarang.domain.jointdelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Store {
    @Id
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Builder
    public Store(String storeName, String image, String address, Double latitude, Double longitude) {
        this.storeName = storeName;
        this.image = image;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }
}
