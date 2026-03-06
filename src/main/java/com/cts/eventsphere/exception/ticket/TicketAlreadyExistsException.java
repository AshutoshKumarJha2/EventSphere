package com.cts.eventsphere.exception.ticket;

/**
 * Exception raised when ticket already exists
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public class TicketAlreadyExistsException extends RuntimeException {
    public TicketAlreadyExistsException(String message) {
        super(message);
    }
}
