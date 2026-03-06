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
 * Workflow: Organizer selects venue -> Booking created (Pending)
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
     */
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingRequestDto) {
        log.info("REST request to create Booking. Request Data: {}", bookingRequestDto);

        BookingResponseDto response = bookingService.createBooking(bookingRequestDto);

        log.info("Booking created successfully. Response Data: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves all bookings.
     */
    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        log.info("REST request to fetch all bookings");

        List<BookingResponseDto> bookings = bookingService.getAllBookings();

        log.info("Successfully fetched {} bookings", bookings.size());
        return ResponseEntity.ok(bookings);
    }

    /**
     * Updates the status of a specific booking.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponseDto> updateStatus(
            @PathVariable("id") String id,
            @RequestParam("newStatus") BookingStatus newStatus) {

        log.info("REST request to update status for Booking ID: {} to {}", id, newStatus);

        BookingResponseDto updatedBooking = bookingService.updateBookingStatus(id, newStatus);

        log.info("Booking status updated successfully. Response Data: {}", updatedBooking);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Deletes a booking record.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") String bookingId) {
        log.info("REST request to delete booking with ID: {}", bookingId);

        bookingService.deleteBooking(bookingId);

        log.info("Booking with ID: {} deleted successfully", bookingId);
        return ResponseEntity.noContent().build();
    }
}