package com.devcrew.usermicroservice.utils;

import com.devcrew.usermicroservice.exception.BadCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ValidationUtils.
 * This class contains tests for the validation utility methods.
 */
@ActiveProfiles("test")
@SpringBootTest
public class ValidationUtilsTest {

    /**
     * Test for isNullOrEmpty method with an empty string.
     * This test verifies that the method returns true for an empty string.
     */
    @Test
    public void testIsNullOrEmpty_String_Empty() {
        assertTrue(ValidationUtils.isNullOrEmpty(""));
    }

    /**
     * Test for isNullOrEmpty method with a non-empty string.
     * This test verifies that the method returns false for a non-empty string.
     */
    @Test
    public void testIsNullOrEmpty_String_NotEmpty() {
        assertFalse(ValidationUtils.isNullOrEmpty("not empty"));
    }

    /**
     * Test for isEmailValid method with a valid email.
     * This test verifies that no exception is thrown for a valid email.
     */
    @Test
    public void testIsEmailValid_ValidEmail() {
        assertDoesNotThrow(() -> ValidationUtils.isEmailValid("test@example.com"));
    }

    /**
     * Test for isEmailValid method with an invalid email.
     * This test verifies that a BadCredentialsException is thrown for an invalid email.
     */
    @Test
    public void testIsEmailValid_InvalidEmail() {
        assertThrows(BadCredentialsException.class, () -> ValidationUtils.isEmailValid("invalid-email"));
    }

    /**
     * Test for isLoginIdEmail method with a valid email.
     * This test verifies that the method returns true for a valid email.
     */
    @Test
    public void testIsLoginIdEmail_ValidEmail() {
        assertTrue(ValidationUtils.isLoginIdEmail("test@example.com"));
    }

    /**
     * Test for isLoginIdEmail method with an invalid email.
     * This test verifies that the method returns false for an invalid email.
     */
    @Test
    public void testIsLoginIdEmail_InvalidEmail() {
        assertFalse(ValidationUtils.isLoginIdEmail("username"));
    }
}
