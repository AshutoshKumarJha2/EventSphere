package com.cts.eventsphere.dto.resource;


import java.util.List;

/**
 * Data Transfer Object representing a specific resource allocation request.
 */
public record ResourceAllocationRequestDto(
        String eventId,
        String venueId,
        String bookingId,
        List<ResourceListElementDto> resourceListElement
) {
}