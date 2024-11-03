package com.devcrew.usermicroservice.exception;

/**
 * Exception thrown when the user already exists
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Constructor
     * @param message the message to be shown
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
