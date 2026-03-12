package com.cts.eventsphere.exception.user;

/**
 * Exception thrown when a user is not found in the system.
 * * @author 2480010
 *
 * @version 1.0
 * @since 10-03-2026
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User with ID " + userId + " does not exist.");
    }
}
