package com.cts.eventsphere.exception.user;

/**
 * Exception for handling situations when user enters an invalid password
 * * @author 2480010
 *
 * @version 1.0
 * @since 10-03-2026
 */
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
