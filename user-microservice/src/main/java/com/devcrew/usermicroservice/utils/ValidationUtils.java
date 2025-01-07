package com.devcrew.usermicroservice.utils;

import com.devcrew.usermicroservice.exception.BadCredentialsException;


/**
 * ValidationUtils class is used to validate the input data.
 * It checks if the input data is null or empty.
 * It also checks if the email is valid.
 * It also checks if the loginId is and email, if not, then it is username.
 */
public class ValidationUtils {

    /**
     * This method checks if the string is null or empty.
     * @param str is the string to be checked.
     * @return true if the string is null or empty, false otherwise.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }


    /**
     * This method checks if the object is null or empty.
     * @param obj is the object to be checked.
     * @return true if the object is null or empty, false otherwise.
     */
    public static boolean isNullOrEmpty(Object obj) {
        return obj == null;
    }

    /**
     * This method checks if the email is valid. If it's not valid, then it throws an exception.
     * @param email is the email to be checked.
     */
    public static boolean isEmailValid(String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new BadCredentialsException("Invalid email");
        }
        return true;
    }

    /**
     * This method checks if the loginId (Identifier for loñgin) is an email.
     * If it's not an email, then it's a username.
     * @param loginId is the loginId to be checked.
     * @return true if the loginId is an email, false otherwise.
     */
    public static boolean isLoginIdEmail(String loginId) {
        return loginId.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * This method checks if the password is valid.
     * The password must contain at least one digit, one lowercase letter, one uppercase letter, and no whitespace.
     * @param password is the password to be checked.
     * @return true if the password is valid, false otherwise.
     */
    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
    }
}
