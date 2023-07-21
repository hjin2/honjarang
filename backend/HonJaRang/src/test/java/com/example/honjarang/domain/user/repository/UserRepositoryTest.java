package com.example.honjarang.domain.user.repository;

import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/data-test.sql")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final Long TEST_ID = 1L;
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "test1234";
    private static final String TEST_NICKNAME = "test";
    private static final String TEST_PROFILE_IMAGE = "test.png";
    private static final Integer TEST_POINT = 0;
    private static final String TEST_ADDRESS = "test";
    private static final Double TEST_LATITUDE = 0.0;
    private static final Double TEST_LONGITUDE = 0.0;
    private static final Role TEST_ROLE = Role.ROLE_USER;
    private static final Boolean TEST_IS_DELETED = false;

    @Test
    @DisplayName("이메일로 사용자 조회")
    void findByEmail() {
        // given

        // when
        Optional<User> user = userRepository.findByEmail(TEST_EMAIL);

        // then
        assertTrue(user.isPresent());
        assertEquals(TEST_ID, user.get().getId());
        assertEquals(TEST_EMAIL, user.get().getEmail());
        assertEquals(TEST_PASSWORD, user.get().getPassword());
        assertEquals(TEST_NICKNAME, user.get().getNickname());
        assertEquals(TEST_PROFILE_IMAGE, user.get().getProfileImage());
        assertEquals(TEST_POINT, user.get().getPoint());
        assertEquals(TEST_ADDRESS, user.get().getAddress());
        assertEquals(TEST_LATITUDE, user.get().getLatitude());
        assertEquals(TEST_LONGITUDE, user.get().getLongitude());
        assertEquals(TEST_ROLE, user.get().getRole());
        assertEquals(TEST_IS_DELETED, user.get().getIsDeleted());
    }
}