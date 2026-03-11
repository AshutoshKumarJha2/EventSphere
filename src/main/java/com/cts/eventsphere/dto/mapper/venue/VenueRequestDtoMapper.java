package com.cts.eventsphere.dto.mapper.venue;

import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.model.Venue;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  DtoMapper from dta to Venue
 *
 * @author 2479476
 * @version 1.0
 * @since 2-03-2026
 */

@Component
public class VenueRequestDtoMapper {
    public Venue toEntity(VenueRequestDto dto){
        Venue venue = new Venue();


        venue.setName(dto.name());
        venue.setLocation(dto.location());
        venue.setCapacity(dto.capacity());
        venue.setAvailabilityStatus(dto.availabilityStatus());
        venue.setCreatedAt(LocalDateTime.now());

        return  venue;
    }
}
