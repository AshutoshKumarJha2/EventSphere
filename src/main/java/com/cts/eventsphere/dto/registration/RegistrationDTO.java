package com.cts.eventsphere.dto.registration;

/**
 * DTO object for registration response
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public record RegistrationDTO(
        String registrationId,
        String eventId,
        String ticketId,
        String attendeeId,
        String status
) {
}
