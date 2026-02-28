package com.cts.eventsphere.dto.event;

import com.cts.eventsphere.model.data.EventStatus;

import java.time.LocalDateTime;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2479623
 *
 * @version 1.0
 * @since 28-02-2026
 */
public record EventRequestDto(
        String name,
        String organizerId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String venueId,
        EventStatus status
) {
}
