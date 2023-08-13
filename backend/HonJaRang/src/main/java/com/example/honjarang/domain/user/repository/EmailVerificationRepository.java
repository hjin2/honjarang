package com.example.honjarang.domain.user.repository;

import com.example.honjarang.domain.user.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmail(String email);
    void deleteByExpiredAtBefore(LocalDateTime now);
}
