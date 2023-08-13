package com.example.honjarang.domain.jointpurchase.repository;

import com.example.honjarang.domain.jointpurchase.dto.JointPurchaseListDto;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchaseApplicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JointPurchaseApplicantRepository extends JpaRepository<JointPurchaseApplicant, Long> {
    List<JointPurchaseApplicant> findAllByJointPurchaseId(Long jointPurchaseId);
    Optional<JointPurchaseApplicant> findByJointPurchaseIdAndUserId(Long jointPurchaseId, Long userId);
    Integer countByJointPurchaseId(Long jointPurchaseId);
    boolean existsByJointPurchaseIdAndUserId(Long jointPurchaseId, Long userId);

    Page<JointPurchaseApplicant> findAllByUserId(Long jointPurchaseId, Pageable pageable);

    Integer countAllByUserId(Long id);
}
