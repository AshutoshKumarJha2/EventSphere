package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.venue.VenueRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueResponseDtoMapper;
import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.exception.venue.VenueNotFoundException;
import com.cts.eventsphere.model.Venue;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.repository.VenueRepository;
import com.cts.eventsphere.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for Venue related operations.
 * Handles business logic for venue registration, updates, and filtering.
 *
 * @author 2479476
 * @since 05-03-2026
 */
@RequiredArgsConstructor
@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final VenueRequestDtoMapper venueRequestDtoMapper;
    private final VenueResponseDtoMapper venueResponseDtoMapper;

    /**
     * Converts a list of Venue entities into a list of VenueResponseDto objects.
     *
     * @param venueList the list of venue entities to convert
     * @return a list of mapped venue response DTOs
     */
    public List<VenueResponseDto> convertHelper(List<Venue> venueList){
        List<VenueResponseDto> convertedList = new ArrayList<>();
        for(Venue v : venueList){
            convertedList.add(venueResponseDtoMapper.toDto(v));
        }
        return convertedList;
    }

    /**
     * Saves a new venue to the repository.
     *
     * @param dto the venue request data transfer object
     * @return the saved venue as a response DTO
     */
    @Override
    public VenueResponseDto create(VenueRequestDto dto) {
        Venue venue = venueRequestDtoMapper.toEntity(dto);
        Venue savedVenue = venueRepository.save(venue);
        return venueResponseDtoMapper.toDto(savedVenue);
    }

    /**
     * Retrieves all venues available in the system.
     *
     * @return a list of all venue response DTOs
     */
    @Override
    public List<VenueResponseDto> findAll() {
        List<Venue> venues = venueRepository.findAll();
        return convertHelper(venues);
    }

    /**
     * Retrieves venues filtered by a specific geographic location.
     *
     * @param location the location string to search for
     * @return a list of venues matching the location
     */
    @Override
    public List<VenueResponseDto> findByLocation(String location) {
        List<Venue> locationList = venueRepository.findByLocation(location);
        return convertHelper(locationList);
    }

    /**
     * Updates an existing venue's information.
     *
     * @param venueId the ID of the venue to update
     * @param dto the new venue details
     * @return the updated venue response DTO
     * @throws VenueNotFoundException if no venue exists with the given ID
     */
    @Override
    public VenueResponseDto updateVenue(String venueId, VenueRequestDto dto) {
        Venue existingVenue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException("Venue not found with id: " + venueId));

        Venue updatedVenue = venueRequestDtoMapper.toEntity(dto);
        updatedVenue.setVenueId(existingVenue.getVenueId());

        return venueResponseDtoMapper.toDto(venueRepository.save(updatedVenue));
    }

    /**
     * Updates only the availability status of a specific venue.
     *
     * @param venueId the ID of the venue
     * @param status the new availability status (e.g., available, maintenance)
     * @return the updated venue response DTO
     * @throws VenueNotFoundException if no venue exists with the given ID
     */
    @Override
    public VenueResponseDto updateVenueStatus(String venueId, AvailabilityStatus status) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException("Venue not found with id: " + venueId));

        venue.setAvailabilityStatus(status);
        return venueResponseDtoMapper.toDto(venueRepository.save(venue));
    }

    /**
     * Deletes a venue from the system by its ID.
     *
     * @param venueId the ID of the venue to be removed
     * @throws VenueNotFoundException if no venue exists with the given ID
     */
    @Override
    public void deleteVenue(String venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new VenueNotFoundException("Venue not found with id: " + venueId);
        }
        venueRepository.deleteById(venueId);
    }

    /**
     * Finds venues that are free on a specific date.
     *
     * @param date the date string in yyyy-MM-dd format
     * @return a list of available venues for that date
     * @throws IllegalArgumentException if the date format is invalid
     */
    @Override
    public List<VenueResponseDto> findByDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);

            List<Venue> freeVenues = venueRepository.findAvailableVenues(
                    localDate,
                    AvailabilityStatus.available,
                    null
            );

            return convertHelper(freeVenues);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd", e);
        }
    }

    /**
     * Retrieves venues that have a capacity equal to or greater than the specified amount.
     *
     * @param capacity the minimum capacity required
     * @return a list of venues meeting the capacity criteria
     */
    @Override
    public List<VenueResponseDto> findByCapacity(int capacity) {
        List<Venue> byCapacityList = venueRepository.findByCapacityGreaterThanEqual(capacity);
        return convertHelper(byCapacityList);
    }

    /**
     * Filters venues based on their current availability status.
     *
     * @param status the status to filter by
     * @return a list of venues matching the status
     */
    @Override
    public List<VenueResponseDto> findByAvailablityStatus(AvailabilityStatus status) {
        List<Venue> byStatus = venueRepository.findByAvailabilityStatus(status);
        return convertHelper(byStatus);
    }
}