package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.resource.ResourceRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.resource.ResourceResponseDtoMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public ResourceResponseDto createResource(String venueId,ResourceRequestDto resourceRequestDto) {
        log.info("Initiating resource creation: {}", resourceRequestDto.name());

        Resource resource = ResourceRequestDtoMapper.toEntity(resourceRequestDto);

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> {
                    log.error("Venue ID {} not found during resource creation", venueId);
                    return new RuntimeException("Venue not found with id: " + venueId);
                });

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

        Event event = eventRepository.getReferenceById(eventId);
        Venue venue = venueRepository.getReferenceById(venueId);

        for (ResourceListElementDto resourceReq : resources) {
            Resource resource = resourceRepository.findByName(resourceReq.resourceName());

            if (resource == null) {
                log.error("Allocation failed: Resource '{}' not found", resourceReq.resourceName());
                throw new ResourceNotFoundException("Resource not found: " + resourceReq.resourceName());
            }

            if (resource.getUnit() < resourceReq.quantity()) {
                log.error("Allocation failed: Insufficient units for {}. Available: {}, Requested: {}",
                        resource.getName(), resource.getUnit(), resourceReq.quantity());
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

            resourceAllocationRepository.save(resourceAllocation);
            log.debug("Allocated {} units of '{}' to Event: {}", resourceReq.quantity(), resource.getName(), eventId);
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