package com.cts.eventsphere.dto.venue;

import com.cts.eventsphere.model.data.AvailabilityStatus;
import jakarta.validation.constraints.*;

/**
 * DTO for Request of Venue
 * * @author 2479476
 * @version 1.1
 * @since 2-03-2026
 */
public record VenueRequestDto(
        @NotBlank(message = "Venue name cannot be empty")
        @Size(min = 2, max = 100, message = "Venue name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Location is required")
        @Size(max = 255, message = "Location address is too long")
        String location,

        @Positive(message = "Capacity must be greater than zero")
        @Max(value = 1000000, message = "Capacity exceeds maximum limit")
        int capacity,

        AvailabilityStatus availabilityStatus
) {
}