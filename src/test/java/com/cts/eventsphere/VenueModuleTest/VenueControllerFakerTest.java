package com.cts.eventsphere.VenueModuleTest;

import com.cts.eventsphere.controller.VenueController;
import com.cts.eventsphere.dto.venue.VenueRequestDto;
import com.cts.eventsphere.dto.venue.VenueResponseDto;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.service.impl.VenueServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
 class VenueControllerFakerTest {

    private MockMvc mockMvc;
    private Faker faker;

    @Mock
    private VenueServiceImpl venueService;

    @InjectMocks
    private VenueController venueController;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        mockMvc = MockMvcBuilders.standaloneSetup(venueController).build();
    }

    @Test
    void addVenue_ShouldReturnCreated() throws Exception {
        // Since VenueResponseDto is a RECORD, use the constructor to set values
        VenueResponseDto responseDto = new VenueResponseDto(
                faker.idNumber().valid(),
                faker.university().name(),
                faker.address().city(),
                faker.number().numberBetween(50, 5000),
                AvailabilityStatus.available
        );

        when(venueService.create(any(VenueRequestDto.class))).thenReturn(responseDto);

        // Simulated JSON payload for Request
        String requestJson = String.format("{\"name\":\"%s\", \"location\":\"%s\", \"capacity\":%d}",
                responseDto.name(), responseDto.location(), responseDto.capacity());

        mockMvc.perform(post("/api/v1/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.name").value(responseDto.name()));
    }

    @Test
    void getAllVenue_ShouldReturnList() throws Exception {
        VenueResponseDto responseDto = new VenueResponseDto(
                faker.idNumber().valid(),
                faker.company().name(),
                faker.address().city(),
                200,
                AvailabilityStatus.available
        );

        when(venueService.findAll()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/venues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(responseDto.name()))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getVenueByLocation_ShouldReturnVenues() throws Exception {
        String city = faker.address().city();
        VenueResponseDto responseDto = new VenueResponseDto(
                faker.idNumber().valid(),
                faker.company().name(),
                city,
                300,
                AvailabilityStatus.available
        );

        when(venueService.findByLocation(city)).thenReturn(Collections.singletonList(responseDto));

        mockMvc.perform(get("/api/v1/venues/location/{location}", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value(city))
                .andExpect(jsonPath("$[0].name").value(responseDto.name()));
    }
}