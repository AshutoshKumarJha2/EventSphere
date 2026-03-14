package com.cts.eventsphere.dto.mapper.booking;

import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.dto.booking.BookingResponseVenueManagerDto;
import com.cts.eventsphere.dto.resource.ResourceListElementDto;
import com.cts.eventsphere.dto.resource.ResourceVenueManagerResponseDto;
import com.cts.eventsphere.model.Booking;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingRepsonseVenueManagerDtoMapper {
    public BookingResponseVenueManagerDto toDto(Booking booking, List<ResourceVenueManagerResponseDto> resourceReqList) {
        if (booking == null) {
            return null;
        }

        return new BookingResponseVenueManagerDto(
                booking.getBookingId(),
                booking.getEventId(),
                booking.getVenue().getVenueId(),
                booking.getDate(),
                booking.getStatus(),
                resourceReqList,
                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }
}
