package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.resource.ResourceAllocationRequestDto;
import com.cts.eventsphere.dto.resource.ResourceRequestDto;
import com.cts.eventsphere.dto.resource.ResourceResponseDto;
import com.cts.eventsphere.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Resource Entity management and allocation.
 *
 * @author 2479476
 * @since 05-03-2026
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * Create a new resource associated with a specific venue.
     * Restricted to Venue Managers.
     *
     * @param venueId the unique identifier of the venue
     * @param requestDto the data transfer object containing resource details
     * @return the created resource details wrapped in a ResponseEntity
     */
    @PostMapping("/venues/{venueId}/resources")
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<ResourceResponseDto> createResource(
            @PathVariable String venueId,
            @RequestBody ResourceRequestDto requestDto) {
        log.info("REST request to create resource for venue {}: {}", venueId, requestDto);
        ResourceResponseDto response = resourceService.createResource(venueId, requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieve all resources in the system.
     *
     * @return a list of all resource response DTOs
     */
    @GetMapping("/resources")
    public ResponseEntity<List<ResourceResponseDto>> getAllResources() {
        log.info("REST request to get all resources");
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    /**
     * Get a specific resource by its unique ID.
     *
     * @param resourceId the unique identifier of the resource
     * @return the resource details wrapped in a ResponseEntity
     */
    @GetMapping("/resources/{resourceId}")
    public ResponseEntity<ResourceResponseDto> getResourceById(@PathVariable String resourceId) {
        log.info("REST request to get resource by ID: {}", resourceId);
        return ResponseEntity.ok(resourceService.getResourceById(resourceId));
    }

    /**
     * Get all resources belonging to a specific venue.
     * Accessible by Venue Managers, Organizers, or Admins.
     *
     * @param venueId the unique identifier of the venue
     * @return a list of resources associated with the specified venue
     */
    @GetMapping("/venues/{venueId}/resources")
    @PreAuthorize("hasRole('VENUE_MANAGER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<ResourceResponseDto>> getResourcesByVenue(@PathVariable String venueId) {
        log.info("REST request to get resources for venue ID: {}", venueId);
        return ResponseEntity.ok(resourceService.getResourcesByVenue(venueId));
    }

    /**
     * Request resource allocation for a booking.
     * Restricted to Organizers.
     *
     * @param requestDto the allocation request details
     * @return a success message string wrapped in a ResponseEntity
     */
    @PostMapping("/resources/allocation")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<String> requestAllocation(@RequestBody ResourceAllocationRequestDto requestDto) {
        log.info("REST request to allocate resources: {}", requestDto);
        resourceService.requestAllocation(
                requestDto.bookingId(),
                requestDto.eventId(),
                requestDto.venueId(),
                requestDto.resourceListElement()
        );
        return new ResponseEntity<>("Resource Requested", HttpStatus.CREATED);
    }

    /**
     * Approve a resource allocation and deduct units from inventory.
     * Restricted to Venue Managers.
     *
     * @param allocationId the unique identifier of the allocation request
     * @return a confirmation message string wrapped in a ResponseEntity
     */
    @PatchMapping("/resources/allocation/{allocationId}/approve")
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<String> approveAllocation(@PathVariable String allocationId) {
        log.info("REST request to approve allocation: {}", allocationId);
        resourceService.approveAllocation(allocationId);
        return ResponseEntity.ok("Resource allocation approved and inventory updated.");
    }

    /**
     * Update an existing resource.
     * Restricted to Venue Managers.
     *
     * @param resourceId the unique identifier of the resource to update
     * @param requestDto the updated resource details
     * @return the updated resource response DTO
     */
    @PutMapping("/resources/{resourceId}")
    @PreAuthorize("hasRole('VENUE_MANAGER')")
    public ResponseEntity<ResourceResponseDto> updateResource(
            @PathVariable String resourceId,
            @RequestBody ResourceRequestDto requestDto) {
        log.info("REST request to update resource ID: {}", resourceId);
        return ResponseEntity.ok(resourceService.updateResource(resourceId, requestDto));
    }

    /**
     * Delete a resource from the system.
     * Restricted to Venue Managers or Admins.
     *
     * @param resourceId the unique identifier of the resource to delete
     * @return an empty ResponseEntity with No Content status
     */
    @DeleteMapping("/resources/{resourceId}")
    @PreAuthorize("hasRole('VENUE_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteResource(@PathVariable String resourceId) {
        log.warn("REST request to delete resource ID: {}", resourceId);
        resourceService.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }
}