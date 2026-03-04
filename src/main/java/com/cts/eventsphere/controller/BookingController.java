package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.model.data.BookingStatus;
import com.cts.eventsphere.service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Booking API Endpoints
 * * Workflow: Organizer selects venue -> Booking created (Pending)
 *
 * @author 2479476
 * @version 1.0
 * @since 04-03-2026
 */
@RestController
@RequestMapping("/api/v1/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * Creates a new booking.
     * Initial status is set to 'pending' within the service layer.
     */
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking( @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Received request to create booking for Event: {} and Venue: {}",
                bookingRequestDto.eventId(), bookingRequestDto.venueId());

        BookingResponseDto response = bookingService.createBooking(bookingRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * Retrieves all bookings.
     */
    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        log.info("Fetching all bookings");
        List<BookingResponseDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponseDto> updateStatus(
            @PathVariable("id") String id,
            @RequestParam("newStatus") BookingStatus newStatus) {

        log.info("Request to update status for Booking ID: {} to {}", id, newStatus);

        BookingResponseDto updatedBooking = bookingService.updateBookingStatus(id, newStatus);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Deletes a booking record.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") String bookingId) {
        log.info("Deleting booking with ID: {}", bookingId);
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}