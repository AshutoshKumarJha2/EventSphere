package com.cts.eventsphere.VenueModuleTest;

import com.cts.eventsphere.dto.mapper.venue.VenueRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueResponseDtoMapper;
import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.Venue;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.repository.VenueRepository;
import com.cts.eventsphere.service.impl.VenueServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class VenueServiceImplFakerTest {

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private VenueRequestDtoMapper venueRequestDtoMapper;

    @Mock
    private VenueResponseDtoMapper venueResponseDtoMapper;

    @InjectMocks
    private VenueServiceImpl venueService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    // @Test
    // @DisplayName("Should successfully create a venue and return response DTO")
    // void create_ShouldReturnSavedVenueResponse() {
    //     VenueRequestDto requestDto = new VenueRequestDto(
    //             faker.idNumber().valid(),
    //             faker.university().name(),
    //             faker.address().city(),
    //             faker.number().numberBetween(100, 1000),
    //             AvailabilityStatus.available
    //     );

    //     Venue venueEntity = new Venue();
    //     venueEntity.setName(requestDto.name());

    //     Venue savedVenue = new Venue();
    //     savedVenue.setVenueId(requestDto.venueId());
    //     savedVenue.setName(requestDto.name());


    //     VenueResponseDto expectedResponse = new VenueResponseDto(
    //             savedVenue.getVenueId(),
    //             savedVenue.getName(),
    //             requestDto.location(),
    //             requestDto.capacity(),
    //             requestDto.availabilityStatus()
    //     );

    //     when(venueRequestDtoMapper.toEntity(any(VenueRequestDto.class))).thenReturn(venueEntity);
    //     when(venueRepository.save(any(Venue.class))).thenReturn(savedVenue);
    //     when(venueResponseDtoMapper.toDto(any(Venue.class))).thenReturn(expectedResponse);

    //     VenueResponseDto actualResponse = venueService.create(requestDto);

    //     assertNotNull(actualResponse);
    //     assertEquals(expectedResponse.id(), actualResponse.id());
    //     assertEquals(expectedResponse.name(), actualResponse.name());
    //     verify(venueRepository, times(1)).save(any(Venue.class));
    // }

    @Test
    @DisplayName("Should return a list of venues filtered by capacity")
    void findByCapacity_ShouldReturnFilteredList() {
        int minCapacity = 200;
        Venue venue = new Venue();
        VenueResponseDto responseDto = new VenueResponseDto(
                faker.idNumber().valid(),
                faker.company().name(),
                faker.address().city(),
                250,
                AvailabilityStatus.available
        );

        when(venueRepository.findByCapacityGreaterThanEqual(minCapacity)).thenReturn(List.of(venue));
        when(venueResponseDtoMapper.toDto(venue)).thenReturn(responseDto);

        List<VenueResponseDto> result = venueService.findByCapacity(minCapacity);

        assertFalse(result.isEmpty());
        assertTrue(result.getFirst().capacity() >= minCapacity);
    }

    @Test
    @DisplayName("Should return venues for a specific location")
    void findByLocation_ShouldReturnMappedList() {
        // Arrange
        String location = faker.address().city();
        Venue venue = new Venue();
        VenueResponseDto responseDto = new VenueResponseDto(
                faker.idNumber().valid(),
                faker.company().name(),
                location,
                300,
                AvailabilityStatus.available
        );

        when(venueRepository.findByLocation(location)).thenReturn(List.of(venue));
        when(venueResponseDtoMapper.toDto(venue)).thenReturn(responseDto);


        List<VenueResponseDto> result = venueService.findByLocation(location);

        assertEquals(1, result.size());
        assertEquals(location, result.getFirst().location());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when date format is invalid")
    void findByDate_ShouldThrowException_WhenDateFormatIsInvalid() {

        String invalidDate = "invalid-date-format";


        assertThrows(IllegalArgumentException.class, () -> {
            venueService.findByDate(invalidDate);
        });
    }

    @Test
    @DisplayName("Should return empty list when no venues are found")
    void findAll_ShouldReturnEmptyList() {

        when(venueRepository.findAll()).thenReturn(Collections.emptyList());


        List<VenueResponseDto> result = venueService.findAll();

        assertTrue(result.isEmpty());
        verify(venueResponseDtoMapper, never()).toDto(any());
    }
}