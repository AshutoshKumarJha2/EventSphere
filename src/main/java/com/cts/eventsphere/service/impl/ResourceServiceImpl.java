package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.resource.ResourceRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.resource.ResourceResponseDtoMapper;
import com.cts.eventsphere.dto.resource.ResourceListElementDto;
import com.cts.eventsphere.dto.resource.ResourceRequestDto;
import com.cts.eventsphere.dto.resource.ResourceResponseDto;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.exception.resource.InsufficientResourceException;
import com.cts.eventsphere.exception.resource.ResourceAlreadyExistsException;
import com.cts.eventsphere.exception.resource.ResourceDuplicateAllocationException;
import com.cts.eventsphere.exception.resource.ResourceNotFoundException;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Resource;
import com.cts.eventsphere.model.ResourceAllocation;
import com.cts.eventsphere.model.Venue;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.repository.ResourceAllocationRepository;
import com.cts.eventsphere.repository.ResourceRepository;
import com.cts.eventsphere.repository.VenueRepository;
import com.cts.eventsphere.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing inventory resources and their allocations.
 * * @author 2479476
 * @version 1.0
 * @since 05-03-2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final ResourceAllocationRepository resourceAllocationRepository;

    /**
     * Creates a new resource associated with a specific venue.
     * * @param venueId the unique identifier of the venue
     * @param resourceRequestDto the data transfer object for resource creation
     * @return the created resource response DTO
     * @throws ResourceAlreadyExistsException if a resource with the same name exists
     */
    @Override
    @Transactional
    public ResourceResponseDto createResource(String venueId, ResourceRequestDto resourceRequestDto) {
        log.info("Initiating resource creation: {}", resourceRequestDto.name());

        if (resourceRepository.existsByName(resourceRequestDto.name())) {
            log.warn("Resource creation failed: Name '{}' already exists", resourceRequestDto.name());
            throw new ResourceAlreadyExistsException("Resource already exists with name: " + resourceRequestDto.name());
        }

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> {
                    log.error("Venue ID {} not found during resource creation", venueId);
                    return new RuntimeException("Venue not found with id: " + venueId);
                });

        Resource resource = ResourceRequestDtoMapper.toEntity(resourceRequestDto);

        resource.setVenue(venue);
        Resource savedResource = resourceRepository.save(resource);

        log.info("Resource successfully created with ID: {}", savedResource.getResourceId());
        return ResourceResponseDtoMapper.mapToResponseDto(savedResource);
    }

    /**
     * Retrieves all resources available across the system.
     * * @return a list of all resource response DTOs
     */
    @Override
    public List<ResourceResponseDto> getAllResources() {
        log.info("Fetching all available resources");
        return resourceRepository.findAll().stream()
                .map(ResourceResponseDtoMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Approves an allocation and deducts units from the resource inventory.
     * * @param allocationId the unique identifier of the allocation request
     * @throws InsufficientResourceException if requested units exceed available units
     */
    @Override
    @Transactional
    public void approveAllocation(String allocationId) {
        log.info("Venue Manager approving allocation ID: {}", allocationId);

        ResourceAllocation allocation = resourceAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new RuntimeException("Allocation request not found"));


        Resource resource = allocation.getResource();

        if (resource.getUnit() < allocation.getQuantity()) {
            throw new InsufficientResourceException("Cannot approve: Units no longer available");
        }

        resource.setUnit(resource.getUnit() - allocation.getQuantity());

        resourceRepository.save(resource);
        resourceAllocationRepository.save(allocation);

        log.info("Allocation approved and inventory updated for: {}", resource.getName());
    }

    /**
     * Retrieves resource details by its unique identifier.
     * * @param resourceId the ID of the resource to fetch
     * @return the resource details as a response DTO
     */
    @Override
    public ResourceResponseDto getResourceById(String resourceId) {
        log.info("Fetching resource details for ID: {}", resourceId);
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> {
                    log.error("Resource fetch failed: ID {} does not exist", resourceId);
                    return new RuntimeException("Resource not found with id: " + resourceId);
                });
        return ResourceResponseDtoMapper.mapToResponseDto(resource);
    }

    /**
     * Updates an existing resource's core details.
     * * @param resourceId the unique identifier of the resource
     * @param dto the updated resource information
     * @return the updated resource response DTO
     */
    @Override
    @Transactional
    public ResourceResponseDto updateResource(String resourceId, ResourceRequestDto dto) {
        log.info("Updating resource ID: {}", resourceId);

        Resource existingResource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> {
                    log.error("Update failed: Resource ID {} not found", resourceId);
                    return new RuntimeException("Resource not found with id: " + resourceId);
                });


        existingResource.setName(dto.name());
        existingResource.setType(dto.type());
        existingResource.setCostRate(dto.costRate());
        existingResource.setUnit(dto.unit());

        Resource updatedResource = resourceRepository.save(existingResource);
        log.info("Resource ID: {} updated successfully", resourceId);

        return ResourceResponseDtoMapper.mapToResponseDto(updatedResource);
    }

    /**
     * Deletes a resource from the inventory by its ID.
     * * @param resourceId the unique identifier of the resource to delete
     * @throws ResourceNotFoundException if the resource ID does not exist
     */
    @Override
    @Transactional
    public void deleteResource(String resourceId) {
        log.info("Attempting to delete resource ID: {}", resourceId);

        if (!resourceRepository.existsById(resourceId)) {
            log.warn("Delete aborted: Resource ID {} not found", resourceId);
            throw new ResourceNotFoundException("Resource not found with id: " + resourceId);
        }

        resourceRepository.deleteById(resourceId);
        log.info("Resource ID: {} deleted", resourceId);
    }

    /**
     * Requests allocation of multiple resources for a specific event booking.
     * * @param bookingId the unique identifier of the booking
     * @param eventId the unique identifier of the event
     * @param venueId the unique identifier of the venue
     * @param resources the list of resources and their quantities to allocate
     * @throws EventNotFoundException if the event does not exist
     * @throws ResourceDuplicateAllocationException if a resource is already allocated to the venue
     * @throws InsufficientResourceException if inventory units are insufficient
     */
    @Override
    @Transactional
    public void requestAllocation(String bookingId, String eventId, String venueId, List<ResourceListElementDto> resources) {
        log.info("Processing resource allocation for Event: {} at Venue: {}", eventId, venueId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found: " + venueId));

        for (ResourceListElementDto resourceReq : resources) {
            Resource resource = resourceRepository.findById(resourceReq.resourceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + resourceReq.resourceId()));

            if (resourceAllocationRepository.existsByResourceNameAndVenueVenueId(resource.getName(), venueId)) {
                log.warn("Allocation failed: Resource '{}' is already allocated to Venue '{}'", resource.getName(), venueId);
                throw new ResourceDuplicateAllocationException("Resource '" + resource.getName() + "' is already allocated to this venue.");
            }


            ResourceAllocation resourceAllocation = ResourceAllocation.builder()
                    .resource(resource)
                    .event(event)
                    .venue(venue)
                    .quantity(resourceReq.quantity())
                    .build();

            resourceAllocationRepository.save(resourceAllocation);
        }
        log.info("Resource allocation completed for Event: {}", eventId);
    }

    /**
     * Retrieves all resources belonging to a specific venue.
     * * @param venueId the unique identifier of the venue
     * @return a list of resource response DTOs for the venue
     */
    @Override
    public List<ResourceResponseDto> getResourcesByVenue(String venueId) {
        log.info("Fetching resources for Venue ID: {}", venueId);
        List<Resource> venueResources  =  resourceRepository.findByVenue_VenueId(venueId);
        return venueResources.stream().map( ResourceResponseDtoMapper::mapToResponseDto).collect(Collectors.toList());
    }
}