package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Venue Entity with full Audit support.
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
public class VenueController {

    // Using the interface for better abstraction
    private final VenueService venueService;

    @PostMapping
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<VenueResponseDto> addVenue(
            @RequestBody @Valid VenueRequestDto venueRequestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to add a new venue: {} by actor: {}", venueRequestDto.name(), actorId);
        VenueResponseDto created = venueService.create(actorId, venueRequestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN', 'VENUE_MANAGER')")
    public ResponseEntity<List<VenueResponseDto>> getAllVenue(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("Request to fetch all venues by actor: {}", actorId);
        List<VenueResponseDto> venues = venueService.findAll(actorId);
        return ResponseEntity.ok(venues);
    }

    @PutMapping("/{venueId}")
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<VenueResponseDto> updateVenue(
            @PathVariable String venueId,
            @RequestBody @Valid VenueRequestDto venueRequestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to update venue ID: {} by actor: {}", venueId, actorId);
        VenueResponseDto updated = venueService.updateVenue(actorId, venueId, venueRequestDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{venueId}/status")
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<VenueResponseDto> updateVenueStatus(
            @PathVariable String venueId,
            @RequestParam AvailabilityStatus status,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to update status for venue ID: {} to {} by actor: {}", venueId, status, actorId);
        VenueResponseDto updated = venueService.updateVenueStatus(actorId, venueId, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{venueId}")
    @PreAuthorize("hasAnyRole('VENUE_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteVenue(
            @PathVariable String venueId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("REST request to delete venue ID: {} by actor: {}", venueId, actorId);
        venueService.deleteVenue(actorId, venueId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/location/{location}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VenueResponseDto>> getVenueByLocation(
            @PathVariable String location,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("Searching for venues in location: {} by actor: {}", location, actorId);
        List<VenueResponseDto> venues = venueService.findByLocation(actorId, location);
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/capacity/{capacity}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VenueResponseDto>> getVenueByCapacity(
            @PathVariable int capacity,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("Searching for venues with minimum capacity: {} by actor: {}", capacity, actorId);
        List<VenueResponseDto> venues = venueService.findByCapacity(actorId, capacity);
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VenueResponseDto>> getVenueByStatus(
            @PathVariable AvailabilityStatus status,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("Filtering venues by status: {} by actor: {}", status, actorId);
        List<VenueResponseDto> venues = venueService.findByAvailabilityStatus(actorId, status);
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VenueResponseDto>> getVenueByDate(
            @PathVariable String date,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String actorId = userPrincipal.userId();
        log.info("Checking venue availability for date: {} by actor: {}", date, actorId);
        List<VenueResponseDto> venues = venueService.findByDate(actorId, date);
        return ResponseEntity.ok(venues);
    }
}