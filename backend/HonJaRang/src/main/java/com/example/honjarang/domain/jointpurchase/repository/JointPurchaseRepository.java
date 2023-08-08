package com.example.honjarang.domain.jointpurchase.repository;

import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JointPurchaseRepository extends JpaRepository<JointPurchase, Long> {

    Page<JointPurchase> findAllByDeadlineAfterAndIsCanceledFalseOrderByCreatedAtDesc(LocalDateTime now, Pageable pageable);
    Integer countByIsCanceledFalseAndDeadlineAfter(LocalDateTime now);
}
