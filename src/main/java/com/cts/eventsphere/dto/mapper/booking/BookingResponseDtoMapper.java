package com.cts.eventsphere.dto.mapper.booking;

import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.model.Booking;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert Booking Entity to BookingResponseDto
 *
 * @author 2479476
 * @version 1.0
 * @since 04-03-2026
 */
@Component
public class BookingResponseDtoMapper {

    /**
     * Maps Booking Entity to Response DTO.
     * * @param booking the booking entity from the database
     * @return a BookingResponseDto record
     */
    public BookingResponseDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return new BookingResponseDto(
                booking.getBookingId(),
                booking.getEventId(),
                booking.getVenueId(),
                booking.getDate(),
                booking.getStatus(),
                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }
}