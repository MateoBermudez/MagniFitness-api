package com.devcrew.usermicroservice.utils;

import com.devcrew.usermicroservice.exception.BadCredentialsException;

public class ValidationUtils {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null;
    }

    public static void isEmailValid(String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new BadCredentialsException("Invalid email");
        }
    }

    public static boolean isLoginIdEmail(String loginId) {
        return loginId.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
