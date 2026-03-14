package com.cts.eventsphere.dto.booking;

import com.cts.eventsphere.dto.resource.ResourceListElementDto;
import com.cts.eventsphere.dto.resource.ResourceVenueManagerResponseDto;
import com.cts.eventsphere.model.data.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponseVenueManagerDto(
        String bookingId,
        String eventId,
        String venueId,
        LocalDate date,
        BookingStatus status,
        List<ResourceVenueManagerResponseDto> resourceList,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
