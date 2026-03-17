package com.cts.eventsphere.exception.registration;

/**
 * Exception raised when invalid registration status given
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public class InvalidRegistrationStatusException extends RuntimeException {
    public InvalidRegistrationStatusException(String message) {
        super(message);
    }
}
