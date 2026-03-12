package com.cts.eventsphere.service;


import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.data.AvailabilityStatus;

import java.util.List;

public interface VenueService {

  public  VenueResponseDto create(VenueRequestDto dto);

  public List<VenueResponseDto> findAll();

  public List<VenueResponseDto> findByLocation(String location);

    public VenueResponseDto updateVenue(String venueId, VenueRequestDto dto);

    public VenueResponseDto updateVenueStatus(String venueId, AvailabilityStatus status);

    public void deleteVenue(String venueId);

  public List<VenueResponseDto> findByDate(String date);

  public List<VenueResponseDto> findByCapacity(int capacity);

  public List<VenueResponseDto> findByAvailablityStatus(AvailabilityStatus status);

}
