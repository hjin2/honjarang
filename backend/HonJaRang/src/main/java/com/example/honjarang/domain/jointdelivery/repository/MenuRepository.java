package com.example.honjarang.domain.jointdelivery.repository;

import com.example.honjarang.domain.jointdelivery.document.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends MongoRepository<Menu, Long> {
    List<Menu> findAllByStoreId(Long storeId);
}
