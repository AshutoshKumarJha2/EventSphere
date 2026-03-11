package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.resource.ResourceListElementDto;
import com.cts.eventsphere.dto.resource.ResourceRequestDto;
import com.cts.eventsphere.dto.resource.ResourceResponseDto;

import java.util.List;

/**
 * Service interface for managing resource inventory and allocations.
 */
public interface ResourceService {

    // --- Inventory CRUD Operations ---

    /**
     * Adds a new resource to the inventory.
     */
    ResourceResponseDto createResource(String venueId,ResourceRequestDto resourceRequestDto);

    /**
     * Retrieves all available resources.
     */
    List<ResourceResponseDto> getAllResources();

    /**
     * Finds a specific resource by its UUID.
     */
    ResourceResponseDto getResourceById(String resourceId);

    /**
     * Updates resource details (e.g., cost rate, availability status).
     */
    ResourceResponseDto updateResource(String resourceId, ResourceRequestDto resourceRequestDto);

    /**
     * Removes a resource from the system.
     */
    void deleteResource(String resourceId);


    // --- Allocation & Workflow Operations ---

    /**
     * Initiates a request for resources and notifies the Approval Manager.
     */
    void requestAllocation(String eventId, String bookingId, String venueId, List<ResourceListElementDto> resources);

    /**
     * Retrieves all resources associated with a specific venue.
     */
    List<ResourceResponseDto> getResourcesByVenue(String venueId);
}