package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.service.impl.VenueServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/venue")
@RequiredArgsConstructor
public class VenueController {

    private final VenueServiceImpl venueService;

    @PostMapping
    public ResponseEntity<VenueResponseDto> addVenue(@RequestBody VenueRequestDto venueRequestDto) {
        log.info("REST request to add a new venue: {}", venueRequestDto);
        log.debug("Venue creation payload: {}", venueRequestDto);

        VenueResponseDto created = venueService.create(venueRequestDto);

        log.info("Venue created successfully with ID: {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VenueResponseDto>> getAllVenue() {
        log.info("Request to fetch all venues");
        List<VenueResponseDto> venues = venueService.findAll();
        log.debug("Total venues retrieved: {}", venues);
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByLocation(@PathVariable String location) {
        log.info("Searching for venues in location: {}", location);
        List<VenueResponseDto> venues = venueService.findByLocation(location);
        log.info("Found {} venues in {}", venues.size(), location);
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByCapacity(@PathVariable int capacity) {
        log.info("Searching for venues with minimum capacity: {}", capacity);
        List<VenueResponseDto> venues = venueService.findByCapacity(capacity);
        log.info("Found {} venues meeting capacity {}", venues.size(), capacity);
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByStatus(@PathVariable AvailabilityStatus status) {
        log.info("Filtering venues by status: {}", status);
        List<VenueResponseDto> venues = venueService.findByAvailablityStatus(status);
        log.info("Found {} venues with status {}", venues.size(), status);
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByDate(@PathVariable String date) {
        log.info("Checking venue availability for date: {}", date);
        List<VenueResponseDto> venues = venueService.findByDate(date);
        log.info("Found {} venues available on {}", venues.size(), date);
        return ResponseEntity.ok(venues);
    }
}