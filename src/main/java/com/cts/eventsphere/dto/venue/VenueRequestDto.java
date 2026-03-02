package com.cts.eventsphere.dto.venue;

import com.cts.eventsphere.model.data.AvailabilityStatus;

/**
 * DTO for Request of Venue
 ** @author 2479476
 *
 * @version  1.0
 * @since  2-03-2026
 */


public record VenueRequestDto(
        String venueId,
        String name,
        String location,
        int capacity,
        AvailabilityStatus availabilityStatus
) {

}
