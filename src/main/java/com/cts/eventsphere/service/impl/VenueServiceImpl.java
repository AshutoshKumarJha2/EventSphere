package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.venue.VenueRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueResponseDtoMapper;
import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.Venue;
import com.cts.eventsphere.repository.VenueRepository;
import com.cts.eventsphere.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return List.of();
    }

    @Override
    public List<VenueResponseDto> findByCapacity(int capacity) {
        List<Venue> byCapacityList = venueRepository.findByCapacity(capacity);
        return convertHelper(byCapacityList);
    }
}
