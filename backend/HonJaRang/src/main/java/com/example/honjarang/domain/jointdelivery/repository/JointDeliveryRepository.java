package com.example.honjarang.domain.jointdelivery.repository;

import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JointDeliveryRepository extends JpaRepository<JointDelivery, Long> {

    @Query("SELECT j FROM JointDelivery j " +
            "LEFT JOIN Store s " +
            "ON j.store = s " +
            "WHERE j.isCanceled = false " +
            "AND j.deadline > :now " +
            "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) < 10 " +
            "ORDER BY j.createdAt DESC")
    Page<JointDelivery> findAllByIsCanceledFalseAndDeadlineAfterAndDistanceLessThanOrderByCreatedAtDesc(LocalDateTime now, Double latitude, Double longitude, Pageable pageable);

    Page<JointDelivery> findAllByUserId(Long userId, Pageable pageable);

    Integer countByIsCanceledFalseAndDeadlineAfter(LocalDateTime now);

    Integer countAllByUserId(Long userId);
}
