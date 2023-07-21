package com.example.honjarang.domain.user.repository;

import com.example.honjarang.domain.user.entity.EmailVerification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/data-test.sql")
class EmailVerificationRepositoryTest {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    private static final Long TEST_ID = 1L;
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_CODE = "test";
    private static final Boolean TEST_IS_VERIFIED = false;
    private static final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0);
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0);


    @Test
    @DisplayName("이메일로 이메일 인증 조회")
    void findByEmail() {
        // given

        // when
        Optional<EmailVerification> emailVerification = emailVerificationRepository.findByEmail(TEST_EMAIL);

        // then
        assertTrue(emailVerification.isPresent());
        assertEquals(TEST_ID, emailVerification.get().getId());
        assertEquals(TEST_EMAIL, emailVerification.get().getEmail());
        assertEquals(TEST_CODE, emailVerification.get().getCode());
        assertEquals(TEST_IS_VERIFIED, emailVerification.get().getIsVerified());
        assertEquals(TEST_CREATED_AT, emailVerification.get().getCreatedAt());
        assertEquals(TEST_UPDATED_AT, emailVerification.get().getUpdatedAt());
    }
}