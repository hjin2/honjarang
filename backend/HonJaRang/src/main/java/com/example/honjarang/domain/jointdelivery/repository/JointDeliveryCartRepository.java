package com.example.honjarang.domain.jointdelivery.repository;

import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JointDeliveryCartRepository extends JpaRepository<JointDeliveryCart, Long> {
    List<JointDeliveryCart> findAllByJointDeliveryId(Long jointDeliveryId);
    List<JointDeliveryCart> findAllByJointDeliveryIdAndUserId(Long jointDeliveryId, Long userId);
    boolean existsByJointDeliveryIdAndUserId(Long jointDeliveryId, Long userId);

    @Query("SELECT distinct jointDelivery from JointDeliveryCart where user.id =:id")
    List<JointDelivery> findDistinctJointDeliveryByUserId(@Param("id")Long id, Pageable pageable);


    Optional<JointDeliveryCart> findByJointDeliveryIdAndMenuIdAndUserId(Long jointDeliveryId, String menuId, Long userId);
}
