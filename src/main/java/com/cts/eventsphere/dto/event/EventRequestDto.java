package com.cts.eventsphere.dto.event;

import com.cts.eventsphere.model.data.EventStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO for Request of Event.
 * * @author 2479623
 *
 * @version 1.0
 * @since 28-02-2026
 */
public record EventRequestDto(
        @NotBlank(message = "Event name must not be blank")
        @Size(max = 100, message = "Event name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Organizer ID must not be blank")
        String organizerId,

        @NotNull(message = "Start date must not be null")
        @FutureOrPresent(message = "Start date must be in the present or future")
        LocalDateTime startDate,

        @NotNull(message = "End date must not be null")
        @Future(message = "End date must be in the future")
        LocalDateTime endDate,

        @NotBlank(message = "Venue ID must not be blank")
        String venueId,

        @NotNull(message = "Event status must not be null")
        EventStatus status
) {
}