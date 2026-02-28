package com.cts.eventsphere.dto.event;

import com.cts.eventsphere.model.data.EventStatus;

/**
 * DTO for Event Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
public record EventResponseDto(
        String id,
        String eventName,
        String organizerId,
        String startAt,
        String endAt,
        EventStatus status,
        String venueId
) {
}
