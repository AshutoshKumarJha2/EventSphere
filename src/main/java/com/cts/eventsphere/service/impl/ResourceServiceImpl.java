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

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final ResourceAllocationRepository resourceAllocationRepository;

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

    @Override
    public List<ResourceResponseDto> getAllResources() {
        log.info("Fetching all available resources");
        return resourceRepository.findAll().stream()
                .map(ResourceResponseDtoMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approveAllocation(String allocationId) {
        log.info("Venue Manager approving allocation ID: {}", allocationId);

        ResourceAllocation allocation = resourceAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new RuntimeException("Allocation request not found"));


        Resource resource = allocation.getResource();

        // Logic: Double check inventory at the moment of approval
        if (resource.getUnit() < allocation.getQuantity()) {
            throw new InsufficientResourceException("Cannot approve: Units no longer available");
        }

        // SUBTRACTION HAPPENS HERE
        resource.setUnit(resource.getUnit() - allocation.getQuantity());
//        allocation.setStatus("APPROVED");

        resourceRepository.save(resource);
        resourceAllocationRepository.save(allocation);

        log.info("Allocation approved and inventory updated for: {}", resource.getName());
    }

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

            // --- NEW CHECK: Prevent same resource name at same venue ---
            if (resourceAllocationRepository.existsByResourceNameAndVenueVenueId(resource.getName(), venueId)) {
                log.warn("Allocation failed: Resource '{}' is already allocated to Venue '{}'", resource.getName(), venueId);
                throw new ResourceDuplicateAllocationException("Resource '" + resource.getName() + "' is already allocated to this venue.");
            }
            // -----------------------------------------------------------

            if (resource.getUnit() < resourceReq.quantity()) {
                log.error("Allocation failed: Insufficient units for {}. Available: {}, Requested: {}",
                        resource.getName(), resource.getUnit(), resourceReq.quantity());
                throw new InsufficientResourceException("Insufficient units for: " + resource.getName());
            }

            resource.setUnit(resource.getUnit() - resourceReq.quantity());

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

    @Override
    public List<ResourceResponseDto> getResourcesByVenue(String venueId) {
        log.info("Fetching resources for Venue ID: {}", venueId);
       List<Resource> venueResources  =  resourceRepository.findByVenue_VenueId(venueId);
        return venueResources.stream().map( ResourceResponseDtoMapper::mapToResponseDto).collect(Collectors.toList());
    }


}