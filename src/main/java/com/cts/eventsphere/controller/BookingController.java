package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.dto.booking.BookingResponseVenueManagerDto;
import com.cts.eventsphere.model.data.BookingStatus;
import com.cts.eventsphere.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Booking Entity
 *
 * @author 2479476
 * @since 05-03-2026
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * Initiates a new booking request in the system.
     * Only Organizers can initiate a booking request.
     *
     * @param bookingRequestDto the details of the booking to be created
     * @return the created booking response DTO wrapped in a ResponseEntity
     */
    @PostMapping("/bookings")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingRequestDto) {
        log.info("REST request to create Booking for event: {}", bookingRequestDto);
        BookingResponseDto response = bookingService.createBooking(bookingRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves a high-level overview of all bookings.
     * Restricted to Admins.
     *
     * @return a list of all booking response DTOs
     */
    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        log.info("REST request to fetch all bookings");
        return ResponseEntity.ok(bookingService.getAllBookingsServ());
    }

    /**
     * Retrieves all bookings associated with a specific venue.
     * Restricted to Venue Managers and Admins.
     *
     * @param venueId the unique identifier of the venue
     * @return a list of bookings for the specified venue
     */
    @GetMapping("/venues/{venueId}/bookings")
    @PreAuthorize("hasAnyRole('VENUE_MANAGER', 'ADMIN')")
    public ResponseEntity<List<BookingResponseVenueManagerDto>> getBookingsByVenue(@PathVariable String venueId) {
        log.info("REST request to fetch bookings for venue ID: {}", venueId);
        return ResponseEntity.ok(bookingService.getBookingsByVenue(venueId));
    }

    /**
     * Retrieves bookings associated with a specific event ID.
     * Organizers see bookings for their events; Admins can see all.
     *
     * @param eventId the unique identifier of the event
     * @return a list of bookings for the specified event
     */
    @GetMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByEvent(@PathVariable String eventId) {
        log.info("REST request to fetch bookings by event ID: {}", eventId);
        return ResponseEntity.ok(bookingService.getBookingsByEvent(eventId));
    }

    /**
     * Updates the status of an existing booking.
     * Only Venue Managers or Admins can Accept, Reject, or Update status.
     *
     * @param id the unique identifier of the booking to update
     * @param newStatus the new status to be applied to the booking
     * @return the updated booking response DTO
     */
    @PatchMapping("/bookings/{bookingId}/status")
    @PreAuthorize("hasAnyRole('VENUE_MANAGER', 'ADMIN')")
    public ResponseEntity<BookingResponseDto> updateStatus(
            @PathVariable("bookingId") String id,
            @RequestParam("newStatus") BookingStatus newStatus) {

        log.info("REST request to update status for Booking ID: {} to {}", id, newStatus);
        BookingResponseDto updatedBooking = bookingService.updateBookingStatus(id, newStatus);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Removes a booking record from the system.
     * Restricted to Admins.
     *
     * @param bookingId the unique identifier of the booking to delete
     * @return an empty ResponseEntity with No Content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") String bookingId) {
        log.info("REST request to delete booking with ID: {}", bookingId);
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}