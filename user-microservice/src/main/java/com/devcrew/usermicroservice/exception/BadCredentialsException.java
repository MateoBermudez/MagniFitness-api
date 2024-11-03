package com.devcrew.usermicroservice.exception;

/**
 * Exception thrown when the credentials are invalid
 */
public class BadCredentialsException extends RuntimeException {
    /**
     * Constructor
     * @param message the message to be shown
     */
    public BadCredentialsException(String message) {
        super(message);
    }
}
