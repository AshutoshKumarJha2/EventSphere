package com.cts.eventsphere.exception.schedule;

/**
 * Exception raised when schedule is not found.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(String id) {
        super("Could not find schedule with ID: " + id);
    }
}