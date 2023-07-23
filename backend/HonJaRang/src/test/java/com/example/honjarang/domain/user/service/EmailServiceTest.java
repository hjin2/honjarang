package com.example.honjarang.domain.user.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.example.honjarang.domain.user.entity.EmailVerification;
import com.example.honjarang.domain.user.exception.DuplicateEmailException;
import com.example.honjarang.domain.user.exception.VerificationCodeMismatchException;
import com.example.honjarang.domain.user.exception.VerificationCodeNotFoundException;
import com.example.honjarang.domain.user.repository.EmailVerificationRepository;
import com.example.honjarang.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private AmazonSimpleEmailService amazonSimpleEmailService;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Mock
    private UserRepository userRepository;

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_CODE = "test";

    @Test
    @DisplayName("이메일 인증번호 전송 성공")
    void sendVerificationCode_Success() {
        // given
        given(userRepository.existsByEmail(TEST_EMAIL)).willReturn(false);

        // when
        emailService.sendVerificationCode(TEST_EMAIL);

        // then
    }

    @Test
    @DisplayName("이메일 인증번호 전송 실패 - 이미 가입된 이메일인 경우")
    void sendVerificationCode_DuplicateEmailException() {
        // given
        given(userRepository.existsByEmail(TEST_EMAIL)).willReturn(true);

        // when & then
        assertThrows(DuplicateEmailException.class, () -> emailService.sendVerificationCode(TEST_EMAIL));
    }

    @Test
    @DisplayName("이메일 인증번호 저장")
    void saveVerificationCode() {
        // given
        given(emailVerificationRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when
        emailService.saveVerificationCode(TEST_EMAIL, TEST_CODE);

        // then
    }

    @Test
    @DisplayName("이메일 인증번호 검증 성공")
    void verifyCode_Success() {
        // given
        EmailVerification emailVerification = EmailVerification.builder()
                .email(TEST_EMAIL)
                .code(TEST_CODE)
                .expiredAt(LocalDateTime.now().plusMinutes(1))
                .isVerified(false)
                .build();

        given(emailVerificationRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(emailVerification));

        // when
        Boolean result = emailService.verifyCode(TEST_EMAIL, TEST_CODE);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호가 일치하지 않는 경우")
    void verifyCode_VerificationCodeMismatchException() {
        // given
        EmailVerification emailVerification = EmailVerification.builder()
                .email(TEST_EMAIL)
                .code(TEST_CODE)
                .expiredAt(LocalDateTime.now().plusMinutes(1))
                .isVerified(false)
                .build();

        given(emailVerificationRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(emailVerification));

        // when & then
        assertThrows(VerificationCodeMismatchException.class, () -> emailService.verifyCode(TEST_EMAIL, "wrongCode"));
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호가 만료된 경우")
    void verifyCode_VerificationCodeExpiredException() {
        // given
        EmailVerification emailVerification = EmailVerification.builder()
                .email(TEST_EMAIL)
                .code(TEST_CODE)
                .expiredAt(LocalDateTime.now().minusMinutes(1))
                .isVerified(false)
                .build();

        given(emailVerificationRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(emailVerification));

        // when & then
        assertThrows(VerificationCodeMismatchException.class, () -> emailService.verifyCode(TEST_EMAIL, TEST_CODE));
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 이미 인증된 이메일인 경우")
    void verifyCode_EmailAlreadyVerifiedException() {
        // given
        EmailVerification emailVerification = EmailVerification.builder()
                .email(TEST_EMAIL)
                .code(TEST_CODE)
                .expiredAt(LocalDateTime.now().plusMinutes(1))
                .isVerified(true)
                .build();

        given(emailVerificationRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(emailVerification));

        // when & then
        assertThrows(VerificationCodeMismatchException.class, () -> emailService.verifyCode(TEST_EMAIL, TEST_CODE));
    }

    @Test
    @DisplayName("이메일 인증번호 검증 실패 - 인증번호를 찾을 수 없는 경우")
    void verifyCode_VerificationCodeNotFoundException() {
        // given
        given(emailVerificationRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when & then
        assertThrows(VerificationCodeNotFoundException.class, () -> emailService.verifyCode(TEST_EMAIL, TEST_CODE));
    }
}