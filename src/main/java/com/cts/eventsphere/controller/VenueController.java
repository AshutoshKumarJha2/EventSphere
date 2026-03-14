package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.service.impl.VenueServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  Rest Controller for Venue Entity
 *
 * @author 2479476
 * @since 05-03-2026
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueServiceImpl venueService;

    /**
     * Registers a new venue in the system.
     * Restricted to Venue Managers.
     */
    @PostMapping
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<VenueResponseDto> addVenue(@RequestBody VenueRequestDto venueRequestDto) {
        log.info("REST request to add a new venue: {}", venueRequestDto);
        VenueResponseDto created = venueService.create(venueRequestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves a list of all registered venues.
     * Accessible by Organizers (for browsing) and Admins.
     */
    @GetMapping
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<VenueResponseDto>> getAllVenue() {
        log.info("Request to fetch all venues");
        List<VenueResponseDto> venues = venueService.findAll();
        return ResponseEntity.ok(venues);
    }

    /**
     * Updates full details of an existing venue.
     * Restricted to Venue Managers.
     */
    @PutMapping("/{venueId}")
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<VenueResponseDto> updateVenue(
            @PathVariable String venueId,
            @RequestBody VenueRequestDto venueRequestDto) {
        log.info("REST request to update venue ID: {}", venueId);
        VenueResponseDto updated = venueService.updateVenue(venueId, venueRequestDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Updates only the availability status (e.g., Available, Maintenance).
     * Restricted to Venue Managers.
     */
    @PatchMapping("/{venueId}/status")
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<VenueResponseDto> updateVenueStatus(
            @PathVariable String venueId,
            @RequestParam AvailabilityStatus status) {
        log.info("REST request to update status for venue ID: {} to {}", venueId, status);
        VenueResponseDto updated = venueService.updateVenueStatus(venueId, status);
        return ResponseEntity.ok(updated);
    }

    /**
     * Removes a venue record from the system.
     * Accessible by Venue Managers or Admins.
     */
    @DeleteMapping("/{venueId}")
    @PreAuthorize("hasRole('VENUE_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVenue(@PathVariable String venueId) {
        log.info("REST request to delete venue ID: {}", venueId);
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Filters venues based on their geographic location.
     * Public/Organizer access for discovery.
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByLocation(@PathVariable String location) {
        log.info("Searching for venues in location: {}", location);
        List<VenueResponseDto> venues = venueService.findByLocation(location);
        return ResponseEntity.ok(venues);
    }

    /**
     * Filters venues that can accommodate at least the specified capacity.
     */
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByCapacity(@PathVariable int capacity) {
        log.info("Searching for venues with minimum capacity: {}", capacity);
        List<VenueResponseDto> venues = venueService.findByCapacity(capacity);
        return ResponseEntity.ok(venues);
    }

    /**
     * Filters venues by their current availability status.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByStatus(@PathVariable AvailabilityStatus status) {
        log.info("Filtering venues by status: {}", status);
        List<VenueResponseDto> venues = venueService.findByAvailablityStatus(status);
        return ResponseEntity.ok(venues);
    }

    /**
     * Retrieves venues available on a specific date.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByDate(@PathVariable String date) {
        log.info("Checking venue availability for date: {}", date);
        List<VenueResponseDto> venues = venueService.findByDate(date);
        return ResponseEntity.ok(venues);
    }
}