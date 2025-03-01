package com.example.final_project.service;

import com.example.final_project.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;

    @Value("$(spring.mail.username)")
    private String mailUsername;

    @Async
    public void sendVerificationEmail(String to, String subject, String text) {
        CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(mailUsername);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text, true);

                mailSender.send(message);
            } catch (MessagingException e) {
                throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        });
    }
}
