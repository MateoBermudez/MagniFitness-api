package com.devcrew.usermicroservice.exception;

/**
 * Exception thrown when the user is not authorized
 */
public class UnauthorizedException extends RuntimeException {
    /**
     * Constructor
     * @param message the message to be shown
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
