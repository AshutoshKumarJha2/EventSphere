package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.venue.VenueRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueResponseDtoMapper;
import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.Venue;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.repository.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for VenueServiceImpl using JUnit 5 + Mockito.
 */
@ExtendWith(MockitoExtension.class)
class VenueServiceImplTest {

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private VenueRequestDtoMapper venueRequestDtoMapper;

    @Mock
    private VenueResponseDtoMapper venueResponseDtoMapper;

    @InjectMocks
    private VenueServiceImpl venueService;

    private Venue venue1;
    private Venue venue2;

    private VenueRequestDto requestDto;
    private VenueResponseDto responseDto1;
    private VenueResponseDto responseDto2;

    @BeforeEach
    void setUp() {
        // Domain entities
        venue1 = new Venue();
        venue1.setVenueId("v-1");
        venue1.setName("Grand Hall");
        venue1.setLocation("Chennai");
        venue1.setCapacity(500);
        venue1.setAvailabilityStatus(AvailabilityStatus.available);
        venue1.setCreatedAt(LocalDateTime.now());
        venue1.setUpdatedAt(LocalDateTime.now());

        venue2 = new Venue();
        venue2.setVenueId("v-2");
        venue2.setName("Bay View");
        venue2.setLocation("Chennai");
        venue2.setCapacity(200);
        venue2.setAvailabilityStatus(AvailabilityStatus.maintenance);
        venue2.setCreatedAt(LocalDateTime.now());
        venue2.setUpdatedAt(LocalDateTime.now());

        // Request DTO (record)
        requestDto = new VenueRequestDto(
                null,                // venueId (null for create)
                "Grand Hall",
                "Chennai",
                500,
                AvailabilityStatus.available
        );

        // Response DTOs (records)
        responseDto1 = new VenueResponseDto(
                "v-1",
                "Grand Hall",
                "Chennai",
                500,
                AvailabilityStatus.available
        );

        responseDto2 = new VenueResponseDto(
                "v-2",
                "Bay View",
                "Chennai",
                200,
                AvailabilityStatus.maintenance
        );
    }

    @Test
    void create_shouldMapRequest_SaveEntity_AndReturnMappedResponse() {
        // arrange: map request -> entity, save, map entity -> response
        when(venueRequestDtoMapper.toEntity(requestDto)).thenReturn(venue1);
        when(venueRepository.save(venue1)).thenReturn(venue1);
        when(venueResponseDtoMapper.toDto(venue1)).thenReturn(responseDto1);

        // act
        VenueResponseDto result = venueService.create(requestDto);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("v-1");
        assertThat(result.name()).isEqualTo("Grand Hall");
        assertThat(result.location()).isEqualTo("Chennai");
        assertThat(result.capacity()).isEqualTo(500);
        assertThat(result.availabilityStatus()).isEqualTo(AvailabilityStatus.available);

        // verify interactions
        verify(venueRequestDtoMapper, times(1)).toEntity(requestDto);
        verify(venueRepository, times(1)).save(venue1);
        verify(venueResponseDtoMapper, times(1)).toDto(venue1);
        verifyNoMoreInteractions(venueRepository, venueRequestDtoMapper, venueResponseDtoMapper);
    }

    @Test
    void findAll_shouldFetchAllAndMapEachToResponseDto() {
        // arrange
        when(venueRepository.findAll()).thenReturn(List.of(venue1, venue2));
        when(venueResponseDtoMapper.toDto(venue1)).thenReturn(responseDto1);
        when(venueResponseDtoMapper.toDto(venue2)).thenReturn(responseDto2);

        // act
        List<VenueResponseDto> result = venueService.findAll();

        // assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(responseDto1);
        assertThat(result.get(1)).isEqualTo(responseDto2);

        // verify
        verify(venueRepository, times(1)).findAll();
        verify(venueResponseDtoMapper, times(1)).toDto(venue1);
        verify(venueResponseDtoMapper, times(1)).toDto(venue2);
        verifyNoMoreInteractions(venueRepository, venueResponseDtoMapper);
        verifyNoInteractions(venueRequestDtoMapper);
    }

    @Test
    void findByLocation_shouldQueryRepoAndMapResults() {
        // arrange
        String location = "Chennai";
        when(venueRepository.findByLocation(location)).thenReturn(List.of(venue1, venue2));
        when(venueResponseDtoMapper.toDto(venue1)).thenReturn(responseDto1);
        when(venueResponseDtoMapper.toDto(venue2)).thenReturn(responseDto2);

        // act
        List<VenueResponseDto> result = venueService.findByLocation(location);

        // assert
        assertThat(result).containsExactly(responseDto1, responseDto2);

        // verify
        verify(venueRepository, times(1)).findByLocation(location);
        verify(venueResponseDtoMapper, times(1)).toDto(venue1);
        verify(venueResponseDtoMapper, times(1)).toDto(venue2);
        verifyNoMoreInteractions(venueRepository, venueResponseDtoMapper);
        verifyNoInteractions(venueRequestDtoMapper);
    }

    @Test
    void findByCapacity_shouldQueryRepoAndMapResults() {
        // arrange
        int capacity = 200;
        when(venueRepository.findByCapacityGreaterThanEqual(capacity)).thenReturn(List.of(venue2));
        when(venueResponseDtoMapper.toDto(venue2)).thenReturn(responseDto2);

        // act
        List<VenueResponseDto> result = venueService.findByCapacity(capacity);

        // assert
        assertThat(result).containsExactly(responseDto2);

        // verify
        verify(venueRepository, times(1)).findByCapacityGreaterThanEqual(capacity);
        verify(venueResponseDtoMapper, times(1)).toDto(venue2);
        verifyNoMoreInteractions(venueRepository, venueResponseDtoMapper);
        verifyNoInteractions(venueRequestDtoMapper);
    }

    @Test
    void findByDate_shouldReturnEmptyListUntilImplemented() {
        // act
        List<VenueResponseDto> result = venueService.findByDate("2026-02-27");

        // assert
        assertThat(result).isNotNull().isEmpty();

        // verify no repository interactions
        verifyNoInteractions(venueRepository, venueResponseDtoMapper, venueRequestDtoMapper);
    }
}