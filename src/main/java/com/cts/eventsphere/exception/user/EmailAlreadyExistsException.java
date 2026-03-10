package com.cts.eventsphere.exception.user;

/**
 * This exception is thrown when a user tries to register with an email that already exists in the system.
 * * @author 2480010
 *
 * @version 1.0
 * @since 10-03-2026
 */
public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("Email " + email + " is already registered.");
    }
}
