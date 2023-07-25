package com.example.honjarang.domain.jointdelivery.repository;

import com.example.honjarang.domain.jointdelivery.dto.MenuListDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuListDtoRepository extends MongoRepository<MenuListDto, Long> {
    List<MenuListDto> findAllByStoreId(Long storeId);
}
