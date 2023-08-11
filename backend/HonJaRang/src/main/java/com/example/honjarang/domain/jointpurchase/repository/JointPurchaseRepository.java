package com.example.honjarang.domain.jointpurchase.repository;

import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JointPurchaseRepository extends JpaRepository<JointPurchase, Long> {

    @Query("SELECT j FROM JointPurchase j " +
            "WHERE j.isCanceled = false " +
            "AND j.deadline > :now " +
            "AND j.productName LIKE %:keyword% " +
            "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(j.latitude)) * cos(radians(j.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(j.latitude)))) < 10 " +
            "AND (SELECT COUNT (jp) FROM JointPurchaseApplicant jp WHERE jp.jointPurchase = j) < j.targetPersonCount " +
            "ORDER BY j.createdAt DESC")
    Page<JointPurchase> findAllByIsCanceledFalseAndDeadlineAfterAndDistanceLessThanAndTargetPersonCountGreaterThanOrderByCreatedAtDesc(LocalDateTime now, Double latitude, Double longitude, String keyword, Pageable pageable);
    Integer countByIsCanceledFalseAndDeadlineAfter(LocalDateTime now);
}
