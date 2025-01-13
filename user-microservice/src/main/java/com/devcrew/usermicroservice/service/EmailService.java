package com.devcrew.usermicroservice.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String to, String subject, int code) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);

        // Load HTML template
        ClassPathResource htmlResource = new ClassPathResource("templates/2fa-email.html");
        String htmlContent = StreamUtils.copyToString(htmlResource.getInputStream(), StandardCharsets.UTF_8);

        // Load CSS
        ClassPathResource cssResource = new ClassPathResource("static/2fa-email.css");
        String cssContent = StreamUtils.copyToString(cssResource.getInputStream(), StandardCharsets.UTF_8);

        // Insert CSS into HTML
        htmlContent = htmlContent.replace("</head>", "<style>" + cssContent + "</style></head>");

        // Replace placeholder with actual code
        htmlContent = htmlContent.replace("{{code}}", "" + code);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}