package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
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
        VenueResponseDto created = venueService.create(venueRequestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VenueResponseDto>> getAllVenue() {
        List<VenueResponseDto> venues = venueService.findAll();
        return ResponseEntity.ok(venues);
    }

    /**
     * Find venues by location.
     * Example: GET /api/v1/venue/location/Chennai
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByLocation(@PathVariable String location) {
        List<VenueResponseDto> venues = venueService.findByLocation(location);
        return ResponseEntity.ok(venues);
    }

    /**
     * Find venues by capacity (exact match based on repository method).
     * Example: GET /api/v1/venue/capacity/500
     */
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByCapacity(@PathVariable int capacity) {
        List<VenueResponseDto> venues = venueService.findByCapacity(capacity);
        return ResponseEntity.ok(venues);
    }

    /**
     * Find venues by date (format depends on your domain, currently service returns empty list).
     * Example: GET /api/v1/venue/date/2026-03-03
     *
     * NOTE: You might want to change the service to accept LocalDate or a range.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<VenueResponseDto>> getVenueByDate(@PathVariable String date) {
        List<VenueResponseDto> venues = venueService.findByDate(date);
        return ResponseEntity.ok(venues);
    }
}