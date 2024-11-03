package com.devcrew.usermicroservice.exception;

/**
 * Exception thrown when the request is invalid
 */
public class BadRequestException extends RuntimeException {
    /**
     * Constructor
     * @param message the message to be shown
     */
    public BadRequestException(String message) {
        super(message);
    }
}
