package com.example.honjarang.domain.jointdelivery.repository;

import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JointDeliveryCartRepository extends JpaRepository<JointDeliveryCart, Long> {
    List<JointDeliveryCart> findAllByJointDeliveryId(Long jointDeliveryId);
}
