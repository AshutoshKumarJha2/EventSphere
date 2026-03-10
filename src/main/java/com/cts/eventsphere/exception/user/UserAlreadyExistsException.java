package com.cts.eventsphere.exception.user;

/**
 * Exception raised when a registered user tries to re-register with the same email.
 * * @author 2480010
 *
 * @version 1.0
 * @since 10-03-2026
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("User with email " + email + " already exists.");
    }
}
