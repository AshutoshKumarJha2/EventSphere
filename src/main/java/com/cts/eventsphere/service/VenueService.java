package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.data.AvailabilityStatus;

import java.util.List;

/**
 * Service interface for managing venue-related operations.
 * Integrated with actorId for consistent audit logging across the EventSphere system.
 */
public interface VenueService {

    /**
     * Creates a new venue.
     * @param actorId The unique identifier of the user performing the creation.
     */
    VenueResponseDto create(String actorId, VenueRequestDto dto);

    /**
     * Retrieves all venues.
     * @param actorId The unique identifier of the user requesting the data.
     */
    List<VenueResponseDto> findAll(String actorId);

    /**
     * Finds venues by location.
     */
    List<VenueResponseDto> findByLocation(String actorId, String location);

    /**
     * Updates an existing venue's core details.
     */
    VenueResponseDto updateVenue(String actorId, String venueId, VenueRequestDto dto);

    /**
     * Specifically updates the availability status of a venue.
     */
    VenueResponseDto updateVenueStatus(String actorId, String venueId, AvailabilityStatus status);

    /**
     * Removes a venue from the system.
     */
    void deleteVenue(String actorId, String venueId);

    /**
     * Finds venues available on a specific date.
     */
    List<VenueResponseDto> findByDate(String actorId, String date);

    /**
     * Finds venues that meet a minimum capacity requirement.
     */
    List<VenueResponseDto> findByCapacity(String actorId, int capacity);

    /**
     * Finds venues filtered by their current availability status.
     */
    List<VenueResponseDto> findByAvailabilityStatus(String actorId, AvailabilityStatus status);
}