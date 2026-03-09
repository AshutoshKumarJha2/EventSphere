package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.resource.ResourceAllocationRequestDto;
import com.cts.eventsphere.dto.resource.ResourceRequestDto;
import com.cts.eventsphere.dto.resource.ResourceResponseDto;
import com.cts.eventsphere.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Resource inventory.
 */
@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * Create a new resource in the inventory.
     */
    @PostMapping
    public ResponseEntity<ResourceResponseDto> createResource(@RequestBody ResourceRequestDto requestDto) {
        ResourceResponseDto response = resourceService.createResource(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieve all resources.
     */
    @GetMapping
    public ResponseEntity<List<ResourceResponseDto>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }


    @PostMapping("/allocation")
    public ResponseEntity<String> createResource(@RequestBody ResourceAllocationRequestDto requestDto) {

        resourceService.requestAllocation(requestDto.bookingId(), requestDto.eventId() ,requestDto.venueId(),requestDto.resourceListElement());
        return new ResponseEntity<>("Resource Requested", HttpStatus.CREATED);
    }


    /**
     * Get a specific resource by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponseDto> getResourceById(@PathVariable("id") String resourceId) {
        return ResponseEntity.ok(resourceService.getResourceById(resourceId));
    }

    /**
     * Get resources filtered by Venue ID.
     */
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<ResourceResponseDto>> getResourcesByVenue(@PathVariable String venueId) {
        return ResponseEntity.ok(resourceService.getResourcesByVenue(venueId));
    }

    /**
     * Update an existing resource.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponseDto> updateResource(
            @PathVariable("id") String resourceId,
            @RequestBody ResourceRequestDto requestDto) {
        return ResponseEntity.ok(resourceService.updateResource(resourceId, requestDto));
    }

    /**
     * Delete a resource from the system.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable("id") String resourceId) {
        resourceService.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }
}