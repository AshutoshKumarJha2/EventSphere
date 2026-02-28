package com.cts.eventsphere.dto.mapper.event;

import com.cts.eventsphere.dto.event.EventRequestDto;
import com.cts.eventsphere.model.Event;
import org.springframework.stereotype.Component;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2479623
 *
 * @version 1.0
 * @since 28-02-2026
 */
@Component
public class EventRequestDtoMapper {
    public Event toEntity(EventRequestDto dto) {
        if(dto == null) {
            return null;
        }

        Event event = new Event();
        event.setName(dto.name());
        event.setOrganizerId(dto.organizerId());
        event.setStartDate(dto.startDate());
        event.setEndDate(dto.endDate());
        event.setVenueId(dto.venueId());
        event.setStatus(dto.status());
        return event;
    }
}
