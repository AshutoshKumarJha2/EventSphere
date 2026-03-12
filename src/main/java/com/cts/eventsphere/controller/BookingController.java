package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.model.data.BookingStatus;
import com.cts.eventsphere.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * POST /api/v1/bookings
     * Only Organizers can initiate a booking request.
     */
    @PostMapping("/bookings")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingRequestDto) {
        log.info("REST request to create Booking for event: {}", bookingRequestDto);
        BookingResponseDto response = bookingService.createBooking(bookingRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1
     * High-level overview, usually restricted to Admins.
     */
    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        log.info("REST request to fetch all bookings");
        return ResponseEntity.ok(bookingService.getAllBookingsServ());
    }

    /**
     * GET /api/v1/venues/{venueId}/bookings
     * As per requirement: Restricted to Venue Managers and Admins.
     */
    @GetMapping("/venues/{venueId}/bookings")
    @PreAuthorize("hasAnyRole('VENUE_MANAGER', 'ADMIN')")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByVenue(@PathVariable String venueId) {
        log.info("REST request to fetch bookings for venue ID: {}", venueId);
        return ResponseEntity.ok(bookingService.getBookingsByVenue(venueId));
    }

    /**
     * GET /api/v1/{eventId}
     * Organizers should see bookings for their events; Admins can see all.
     */
    @GetMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByEvent(@PathVariable String eventId) {
        log.info("REST request to fetch bookings by event ID: {}", eventId);
        return ResponseEntity.ok(bookingService.getBookingsByEvent(eventId));
    }

    /**
     * PATCH /api/v1/bookings/{bookingId}/status
     * As per requirement: Only VM/Admin can Accept/Reject/Update status.
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
     * DELETE /api/v1/{id}
     * Usually restricted to Admins or the Organizer who created it.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") String bookingId) {
        log.info("REST request to delete booking with ID: {}", bookingId);
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}