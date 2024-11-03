package com.devcrew.usermicroservice.exception;

/**
 * Exception thrown when the user does not exist
 */
public class UserDoesNotExistException extends RuntimeException {
    /**
     * Constructor
     * @param message the message to be shown
     */
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
