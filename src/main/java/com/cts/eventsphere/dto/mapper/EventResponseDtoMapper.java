package com.cts.eventsphere.dto.mapper;

import com.cts.eventsphere.dto.EventResponseDto;
import com.cts.eventsphere.model.Event;
import org.springframework.stereotype.Component;

/**
 * DTO Mapper for Event Response DTO.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
@Component
public class EventResponseDtoMapper {
    public EventResponseDto toDTO(Event event) {
        return new EventResponseDto(
                event.getEventId(),
                event.getName(),
                event.getOrganizerId(),
                event.getStartDate().toString(), // Or use a DateTimeFormatter
                event.getEndDate().toString(),
                event.getStatus(),
                event.getVenueId()
        );
    }
}
