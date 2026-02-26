package com.cts.eventsphere.exception.event;

/**
 * Exception raised when Event is not found.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String id) {
        super("Could not find event with ID: " + id);
    }
}