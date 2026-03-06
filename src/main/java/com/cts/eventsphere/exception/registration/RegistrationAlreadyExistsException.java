package com.cts.eventsphere.exception.registration;

/**
 * Exception raised when registration already exists
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public class RegistrationAlreadyExistsException extends RuntimeException {
    public RegistrationAlreadyExistsException(String message) {
        super(message);
    }
}
