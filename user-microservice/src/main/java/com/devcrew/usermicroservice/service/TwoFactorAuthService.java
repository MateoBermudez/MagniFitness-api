package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.utils.EncryptionUtils;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorAuthService {
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    private final EmailService emailService;

    @Value("${encryption.secret}")
    private String encryptionSecret;

    @Autowired
    public TwoFactorAuthService(EmailService emailService) {
        this.emailService = emailService;
    }

    public String generateSecretKey() {
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secretKey = key.getKey();
        try {
            return EncryptionUtils.encrypt(secretKey, encryptionSecret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyCode(String encryptedSecretKey, int code) {
        String secretKey;
        try {
            secretKey = EncryptionUtils.decrypt(encryptedSecretKey, encryptionSecret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return gAuth.authorize(secretKey, code);
    }

    public void send2FACode(String email, String encryptedSecretKey) {
        try {
            String secretKey = EncryptionUtils.decrypt(encryptedSecretKey, encryptionSecret);
            int code = gAuth.getTotpPassword(secretKey);
            String subject = "Your 2FA Code";
            emailService.sendVerificationCode(email, subject, code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
