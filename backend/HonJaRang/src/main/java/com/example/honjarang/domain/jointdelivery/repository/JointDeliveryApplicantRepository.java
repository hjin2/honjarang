package com.example.honjarang.domain.jointdelivery.repository;

import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JointDeliveryApplicantRepository extends JpaRepository<JointDeliveryApplicant, Long>{
    boolean existsByJointDeliveryIdAndUserId(Long jointDeliveryId, Long userId);
    @Modifying
    void deleteByJointDeliveryIdAndUserId(Long jointDeliveryId, Long userId);
    Optional<JointDeliveryApplicant> findByJointDeliveryIdAndUserId(Long jointDeliveryId, Long userId);
    Integer countByJointDeliveryId(Long jointDeliveryId);
    Integer countAllByUserId(Long userId);
    List<JointDeliveryApplicant> findAllByJointDeliveryId(Long jointDeliveryId);
}
