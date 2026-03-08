package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.venue.VenueRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueResponseDtoMapper;
import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
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

@RequiredArgsConstructor
@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final VenueRequestDtoMapper venueRequestDtoMapper;
    private final VenueResponseDtoMapper venueResponseDtoMapper;

    public  List<VenueResponseDto> convertHelper(List<Venue> venueList){
        List<VenueResponseDto> convertedList = new ArrayList<>();
        for(Venue v : venueList){
            convertedList.add(venueResponseDtoMapper.toDto(v));
        }

        return convertedList;
    }

    @Override
    public VenueResponseDto create(VenueRequestDto dto) {
        Venue venue = venueRequestDtoMapper.toEntity(dto);
        Venue savedVenue = venueRepository.save(venue);

        return venueResponseDtoMapper.toDto(savedVenue);
    }

    @Override
    public List<VenueResponseDto> findAll() {
        List<Venue> venues = venueRepository.findAll();
        return convertHelper(venues);
    }


    @Override
    public List<VenueResponseDto> findByLocation(String location) {
        List<Venue> locationList = venueRepository.findByLocation(location);
        return convertHelper(locationList);
    }

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

    @Override
    public List<VenueResponseDto> findByCapacity(int capacity) {
        List<Venue> byCapacityList = venueRepository.findByCapacityGreaterThanEqual(capacity);
        return convertHelper(byCapacityList);
    }

    @Override
    public List<VenueResponseDto> findByAvailablityStatus(AvailabilityStatus status) {
        List<Venue> byStatus = venueRepository.findByAvailabilityStatus(status);
        return convertHelper(byStatus);
    }
}
