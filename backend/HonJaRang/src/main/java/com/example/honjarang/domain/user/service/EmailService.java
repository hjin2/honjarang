package com.example.honjarang.domain.user.service;

import com.example.honjarang.domain.user.entity.EmailVerification;
import com.example.honjarang.domain.user.exception.DuplicateEmailException;
import com.example.honjarang.domain.user.exception.VerificationCodeMismatchException;
import com.example.honjarang.domain.user.exception.VerificationCodeNotFoundException;
import com.example.honjarang.domain.user.repository.EmailVerificationRepository;
import com.example.honjarang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.time.LocalDateTime;
import java.util.Random;

@EnableAsync
@RequiredArgsConstructor
@Service
public class EmailService {

    @Value("${aws.ses.sender-email}")
    private String senderEmail;

    private final SesClient sesClient;

    private final EmailVerificationRepository emailVerificationRepository;

    private final UserRepository userRepository;

    @Transactional
    public void sendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();
        SendEmailRequest sendEmailRequest = generateSendEmailRequest(email, verificationCode);
        sesClient.sendEmail(sendEmailRequest);
        saveVerificationCode(email, verificationCode);
    }

    @Transactional
    public void sendVerificationCodeForTest(String email) {
        String verificationCode = "000000";
        saveVerificationCode(email, verificationCode);
    }

    private void saveVerificationCode(String email, String code) {
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .code(code)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .build();
        emailVerificationRepository.findByEmail(email).ifPresent(emailVerificationRepository::delete);
        emailVerificationRepository.save(emailVerification);
    }

    @Transactional
    public void verifyCode(String email, String code) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new VerificationCodeNotFoundException("인증번호를 찾을 수 없습니다."));
        if (emailVerification.getIsVerified()) {
            throw new VerificationCodeMismatchException("이미 인증된 이메일입니다.");
        } else if (emailVerification.getExpiredAt().isBefore(LocalDateTime.now())) {
            emailVerificationRepository.delete(emailVerification);
            throw new VerificationCodeMismatchException("인증번호가 만료되었습니다.");
        } else if (!emailVerification.getCode().equals(code)) {
            throw new VerificationCodeMismatchException("인증번호가 일치하지 않습니다.");
        }
        emailVerification.verify();
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomValue = random.nextInt(36);
            if (randomValue < 10) {
                stringBuilder.append(randomValue);
            } else {
                stringBuilder.append((char) (randomValue + 87));
            }
        }
        return stringBuilder.toString();
    }

    private String generateHtmlBody(String verificationCode) {
        return "<h1>인증번호를 입력해주세요.</h1>" +
                "<p>인증번호: " + verificationCode + "</p>";
    }

    private SendEmailRequest generateSendEmailRequest(String email, String verificationCode) {
        return SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(email).build())
                .source(senderEmail)
                .message(Message.builder()
                        .body(Body.builder()
                                .html(Content.builder()
                                        .charset("UTF-8")
                                        .data(generateHtmlBody(verificationCode))
                                        .build())
                                .build())
                        .subject(Content.builder()
                                .charset("UTF-8")
                                .data("회원가입 인증번호")
                                .build())
                        .build())
                .build();
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredEmailVerifications() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        emailVerificationRepository.deleteByExpiredAtBefore(currentDateTime);
    }
}
