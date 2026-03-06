package com.cts.eventsphere.dto.registration;

/**
 * DTO object for creating registration request
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public record RegistrationRequestDTO(
        String eventId,
        String ticketId
    ) {
}
