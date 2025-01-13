package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.service.TwoFactorAuthService;
import com.devcrew.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/2fa")
public class TwoFactorAuthController {

    private final TwoFactorAuthService twoFactorAuthService;
    private final UserService userService;

    @Autowired
    public TwoFactorAuthController(TwoFactorAuthService twoFactorAuthService, UserService userService) {
        this.twoFactorAuthService = twoFactorAuthService;
        this.userService = userService;
    }

    @PostMapping("/generate-2fa-code")
    public ResponseEntity<String> generate2FACode(@RequestHeader("Authorization") String token) {
        String encryptedSecretKey = twoFactorAuthService.generateSecretKey();
        String email = userService.update2FASecretKey(token, encryptedSecretKey);
        twoFactorAuthService.send2FACode(email, encryptedSecretKey);
        return ResponseEntity.ok("2FA code sent to your email");
    }

    @PostMapping("/verify-2fa-code")
    public ResponseEntity<Boolean> verify2FACode(@RequestParam int code, @RequestHeader("Authorization") String token) {
        String encryptedSecretKey = userService.get2FASecretKey(token);
        boolean result = twoFactorAuthService.verifyCode(encryptedSecretKey, code);
        if (!result) {
            return ResponseEntity.badRequest().build();
        }
        userService.updateUser2FAStatus(token, true);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/reset-2fa")
    public ResponseEntity<String> reset2FA(@RequestHeader("Authorization") String token) {
        userService.reset2FA(token, false);
        return ResponseEntity.ok("2FA reset successfully");
    }
}