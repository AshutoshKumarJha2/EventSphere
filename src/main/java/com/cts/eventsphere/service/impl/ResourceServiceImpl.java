package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.resource.ResourceRequestDtoMapper;
import com.cts.eventsphere.dto.resource.ResourceAllocationRequestDto;
import com.cts.eventsphere.dto.resource.ResourceListElementDto;
import com.cts.eventsphere.dto.resource.ResourceRequestDto;
import com.cts.eventsphere.dto.resource.ResourceResponseDto;
import com.cts.eventsphere.exception.resource.InsufficientResourceException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private  final EventRepository eventRepository;
    private  final VenueRepository venueRepository;
    private final ResourceAllocationRepository resourceAllocationRepository;

    @Override
    @Transactional
    public ResourceResponseDto createResource(ResourceRequestDto resourceRequestDto) {
        // 1. Map simple fields from DTO to Entity
        Resource resource = ResourceRequestDtoMapper.toEntity(resourceRequestDto);

        Venue venue = venueRepository.findById(resourceRequestDto.venueId())
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + resourceRequestDto.venueId()));

        resource.setVenue(venue);

        Resource savedResource = resourceRepository.save(resource);

        return mapToResponseDto(savedResource);
    }

    @Override
    public List<ResourceResponseDto> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceResponseDto getResourceById(String resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + resourceId));
        return mapToResponseDto(resource);
    }

    @Override
    @Transactional
    public ResourceResponseDto updateResource(String resourceId, ResourceRequestDto dto) {
        // 1. Find the existing resource
        Resource existingResource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + resourceId));

        // 2. Fetch the Venue entity using the ID from the DTO
        Venue venue = venueRepository.findById(dto.venueId())
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + dto.venueId()));

        // 3. Update the fields
        existingResource.setName(dto.name()); // Don't forget the name!
        existingResource.setVenue(venue);      // Pass the Entity, not the String ID
        existingResource.setType(dto.type());
        existingResource.setCostRate(dto.costRate());
        existingResource.setUnit(dto.unit());

        // 4. Save and return
        Resource updatedResource = resourceRepository.save(existingResource);
        return mapToResponseDto(updatedResource);
    }

    @Override
    @Transactional
    public void deleteResource(String resourceId) {
        if (!resourceRepository.existsById(resourceId)) {
            throw new RuntimeException("Resource not found with id: " + resourceId);
        }
        resourceRepository.deleteById(resourceId);
    }

    @Override
    @Transactional
    public void requestAllocation(String bookingId, String eventId, String venueId, List<ResourceListElementDto> resources) {

        // 1. Fetch the Event and Venue references (Proxy objects are sufficient for FK mapping)
        Event event = eventRepository.getReferenceById(eventId);
        Venue venue = venueRepository.getReferenceById(venueId);

        for (ResourceListElementDto resourceReq : resources) {
            Resource resource = resourceRepository.findByName(resourceReq.resourceName());

            if (resource == null) {
                throw new ResourceNotFoundException("Resource not found: " + resourceReq.resourceName());
            }

            if (resource.getUnit() < resourceReq.quantity()) {
                throw new InsufficientResourceException("Not enough units for: " + resourceReq.resourceName());
            }


            resource.setUnit(resource.getUnit() - resourceReq.quantity());
            resourceRepository.save(resource);

            ResourceAllocation resourceAllocation = ResourceAllocation.builder()
                    .resource(resource)
                    .event(event)
                    .venue(venue)
                    .quantity(resourceReq.quantity())
                    .build();

            // Ensure you have a resourceAllocationRepository injected in your service
            resourceAllocationRepository.save(resourceAllocation);
        }
    }

    @Override
    public List<ResourceResponseDto> getResourcesByVenue(String venueId) {
        // You would need to add 'findByVenueId' to your ResourceRepository
        return List.of();
    }

    /**
     * Helper method to map Entity to Response DTO.
     * Usually, you would have a ResourceResponseDtoMapper for this.
     */
    private ResourceResponseDto mapToResponseDto(Resource resource) {
        return new ResourceResponseDto(
                resource.getResourceId(),
                resource.getVenue().getVenueId(),
                resource.getType(),
                resource.getAvailability(),
                resource.getCostRate(),
                1, // Default unit/quantity
                "PENDING"
        );
    }
}