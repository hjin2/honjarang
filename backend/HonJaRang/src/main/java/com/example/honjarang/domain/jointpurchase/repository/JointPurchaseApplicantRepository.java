package com.example.honjarang.domain.jointpurchase.repository;

import com.example.honjarang.domain.jointpurchase.entity.JointPurchaseApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JointPurchaseApplicantRepository extends JpaRepository<JointPurchaseApplicant, Long> {
    List<JointPurchaseApplicant> findAllByJointPurchaseId(Long jointPurchaseId);
    Integer countByJointPurchaseId(Long jointPurchaseId);
}
