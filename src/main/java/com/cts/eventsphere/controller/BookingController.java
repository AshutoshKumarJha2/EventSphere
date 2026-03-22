package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.dto.booking.BookingResponseVenueManagerDto;
import com.cts.eventsphere.model.data.BookingStatus;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
@Validated // Required for validating method parameters like @PathVariable or @RequestParam
public class BookingController {

    private final BookingService bookingService;

    /**
     * Initiates a new booking request in the system.
     * Only Organizers can initiate a booking request.
     *
     * @param bookingRequestDto the details of the booking to be created
     * @param userPrincipal the authenticated user performing the action
     * @return the created booking response DTO wrapped in a ResponseEntity
     */
    @PostMapping("/bookings")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody @Valid BookingRequestDto bookingRequestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to create Booking for event: {} by actor: {}", bookingRequestDto, actorId);
        BookingResponseDto response = bookingService.createBooking(actorId, bookingRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves a high-level overview of all bookings.
     * Restricted to Admins.
     *
     * @param userPrincipal the authenticated user performing the action
     * @return a list of all booking response DTOs
     */
    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to fetch all bookings by actor: {}", actorId);
        return ResponseEntity.ok(bookingService.getAllBookingsServ(actorId));
    }

    /**
     * Retrieves all bookings associated with a specific venue.
     * Restricted to Venue Managers and Admins.
     *
     * @param venueId the unique identifier of the venue
     * @param userPrincipal the authenticated user performing the action
     * @return a list of bookings for the specified venue
     */
    @GetMapping("/venues/{venueId}/bookings")
    @PreAuthorize("hasAnyRole('VENUE_MANAGER', 'ADMIN')")
    public ResponseEntity<List<BookingResponseVenueManagerDto>> getBookingsByVenue(
            @PathVariable @NotBlank String venueId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to fetch bookings for venue ID: {} by actor: {}", venueId, actorId);
        return ResponseEntity.ok(bookingService.getBookingsByVenue(actorId, venueId));
    }

    /**
     * Retrieves bookings associated with a specific event ID.
     * Organizers see bookings for their events; Admins can see all.
     *
     * @param eventId the unique identifier of the event
     * @param userPrincipal the authenticated user performing the action
     * @return a list of bookings for the specified event
     */
    @GetMapping("/bookings/events/{eventId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByEvent(
            @PathVariable @NotBlank String eventId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to fetch bookings by event ID: {} by actor: {}", eventId, actorId);
        return ResponseEntity.ok(bookingService.getBookingsByEvent(actorId, eventId));
    }

    /**
     * Updates the status of an existing booking.
     * Only Venue Managers or Admins can Accept, Reject, or Update status.
     *
     * @param id the unique identifier of the booking to update
     * @param newStatus the new status to be applied to the booking
     * @param userPrincipal the authenticated user performing the action
     * @return the updated booking response DTO
     */
    @PatchMapping("/bookings/{bookingId}/status")
    @PreAuthorize("hasAnyRole('VENUE_MANAGER', 'ADMIN')")
    public ResponseEntity<BookingResponseDto> updateStatus(
            @PathVariable("bookingId") @NotBlank String id,
            @RequestParam("newStatus") @NotNull BookingStatus newStatus,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to update status for Booking ID: {} to {} by actor: {}", id, newStatus, actorId);
        BookingResponseDto updatedBooking = bookingService.updateBookingStatus(actorId, id, newStatus);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Removes a booking record from the system.
     * Restricted to Admins.
     *
     * @param bookingId the unique identifier of the booking to delete
     * @param userPrincipal the authenticated user performing the action
     * @return an empty ResponseEntity with No Content status
     */
    @DeleteMapping("/bookings/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable("id") @NotBlank String bookingId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to delete booking with ID: {} by actor: {}", bookingId, actorId);
        bookingService.deleteBooking(actorId, bookingId);
        return ResponseEntity.noContent().build();
    }
}