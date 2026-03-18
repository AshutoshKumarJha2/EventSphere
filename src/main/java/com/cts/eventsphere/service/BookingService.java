package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.dto.booking.BookingResponseVenueManagerDto;
import com.cts.eventsphere.model.data.BookingStatus;

import java.util.List;

/**
 * Service interface for Booking operations
 *
 * @author 2479476
 * @version 1.0
 * @since 04-03-2026
 */
public interface BookingService {
    // State-changing operations
    BookingResponseDto createBooking(String actorId, BookingRequestDto bookingRequestDto);
    BookingResponseDto updateBookingStatus(String actorId, String bookingId, BookingStatus status);
    void deleteBooking(String actorId, String bookingId);

    // Read operations
    List<BookingResponseDto> getAllBookingsServ(String actorId);
    List<BookingResponseVenueManagerDto> getBookingsByVenue(String actorId, String venueId);
    List<BookingResponseDto> getBookingsByEvent(String actorId, String eventId);
}