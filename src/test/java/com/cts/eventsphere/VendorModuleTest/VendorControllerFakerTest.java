package com.cts.eventsphere.VendorModuleTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

import com.cts.eventsphere.controller.VendorController;
import com.cts.eventsphere.service.VendorService;
import com.cts.eventsphere.dto.vendor.VendorResponseDto;
import com.cts.eventsphere.model.data.VendorStatus;

import com.github.javafaker.Faker;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class VendorControllerFakerTest {
    private MockMvc mockMvc;
    private Faker faker = new Faker();

    @Mock private VendorService vendorService;
    @InjectMocks private VendorController vendorController;

    @BeforeEach
    void setup() { mockMvc = MockMvcBuilders.standaloneSetup(vendorController).build(); }

    @Test
    void getVendorById_ShouldReturnVendor() throws Exception {
        String id = faker.idNumber().valid();
        VendorResponseDto response = new VendorResponseDto(
                id, faker.company().name(), faker.phoneNumber().cellPhone(),
                VendorStatus.active, LocalDateTime.now(), LocalDateTime.now()
        );

        when(vendorService.getVendorById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/vendors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.name()));
    }
}