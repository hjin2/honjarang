package com.example.honjarang.domain.jointdelivery.repository;

import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface JointDeliveryApplicantRepository extends JpaRepository<JointDeliveryApplicant, Long>{
    boolean existsByJointDeliveryIdAndUserId(Long jointDeliveryId, Long userId);

    @Modifying
    void deleteByJointDeliveryIdAndUserId(Long jointDeliveryId, Long userId);
}
