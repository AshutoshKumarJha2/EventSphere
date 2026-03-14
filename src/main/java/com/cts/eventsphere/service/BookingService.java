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

    /**
     * Creates a new booking with an initial status (typically 'pending')
     * * @param bookingRequestDto the booking details from the organizer
     * @return the created booking response
     */
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto);



    List<BookingResponseVenueManagerDto> getBookingsByVenue(String venueId);


    /**
     * Retrieves all bookings in the system
     * * @return list of booking responses
     */
    List<BookingResponseDto> getAllBookingsServ();



    List<BookingResponseDto> getBookingsByEvent(String eventId);

    /**
     * Updates the status of an existing booking (e.g., from pending to confirmed)
     * * @param bookingId the ID of the booking to update
     * @return the updated booking response
     */


    BookingResponseDto updateBookingStatus(String bookingId, BookingStatus status);

    /**
     * Cancels/Deletes a booking record
     * * @param bookingId the ID of the booking to remove
     */
    void deleteBooking(String bookingId);
}