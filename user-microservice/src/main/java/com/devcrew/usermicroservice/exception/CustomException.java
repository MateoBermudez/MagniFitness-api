package com.devcrew.usermicroservice.exception;

/**
 * Custom exception
 */
public class CustomException extends RuntimeException {
    /**
     * Constructor
     * @param message the message to be shown
     */
    public CustomException(String message) {
        super(message);
    }
}
