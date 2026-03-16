package com.cts.eventsphere.dto.booking;

/**
 * Request Dto for creating or updating a Booking
 *
 * @author 2479476
 * @version 1.0
 * @since 04-03-2026
 */
public record BookingRequestDto(
        String eventId,
        String venueId
) {
}