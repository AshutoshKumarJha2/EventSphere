package com.cts.eventsphere.exception.registration;

/**
 * Exception raised when registration not found
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public class RegistrationNotFoundException extends RuntimeException {
    public RegistrationNotFoundException(String message) {
        super(message);
    }
}
