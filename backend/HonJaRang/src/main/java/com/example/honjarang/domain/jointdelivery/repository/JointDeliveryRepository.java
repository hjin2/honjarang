package com.example.honjarang.domain.jointdelivery.repository;

import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JointDeliveryRepository extends JpaRepository<JointDelivery, Long> {
    Page<JointDelivery> findAllByDeadlineAfterAndIsCanceledFalse(LocalDateTime now, Pageable pageable);

    Page<JointDelivery> findAllByUserId(Long userId, Pageable pageable);

}
