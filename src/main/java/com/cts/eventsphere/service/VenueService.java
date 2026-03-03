package com.cts.eventsphere.service;


import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;

import java.util.List;

public interface VenueService {

  public  VenueResponseDto create(VenueRequestDto dto);

  public List<VenueResponseDto> findAll();

  public List<VenueResponseDto> findByLocation(String location);

  public List<VenueResponseDto> findByDate(String date);

  public List<VenueResponseDto> findByCapacity(int capacity);

}
