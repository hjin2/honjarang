package com.example.honjarang.domain.jointpurchase.repository;

import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JointPurchaseRepository extends JpaRepository<JointPurchase, Long> {

    Page<JointPurchase> findAllByDeadlineAfterAndIsCanceledFalse(LocalDateTime now, Pageable pageable);
}
