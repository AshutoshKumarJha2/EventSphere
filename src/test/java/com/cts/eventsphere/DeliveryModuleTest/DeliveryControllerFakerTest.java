package com.cts.eventsphere.DeliveryModuleTest;

import com.cts.eventsphere.controller.DeliveryController;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.model.data.DeliveryStatus;
import com.cts.eventsphere.service.DeliveryService;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerFakerTest {

    private MockMvc mockMvc;
    private final Faker faker = new Faker();

    @Mock
    private DeliveryService deliveryService;

    @InjectMocks
    private DeliveryController deliveryController;

    @BeforeEach
    void setup() {
        // Standalone setup for focused Controller testing
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryController).build();
    }

    @Test
    void updateDeliveryStatus_ShouldReturnUpdatedDto() throws Exception {
        // 1. Arrange
        String deliveryId = "DEL-" + faker.number().digits(5);
        // Use the actual Enum object to ensure the string matches the definition
        DeliveryStatus statusEnum = DeliveryStatus.delivered;

        DeliveryResponseDto response = new DeliveryResponseDto(
                deliveryId,
                "INV-" + faker.number().digits(5),
                faker.commerce().productName(),
                faker.number().numberBetween(1, 10),
                LocalDateTime.now().plusDays(1),
                statusEnum, // The actual Enum object
                "TRK-" + faker.number().digits(10),
                LocalDateTime.now().minusHours(5),
                LocalDateTime.now()
        );

        when(deliveryService.updateDeliveryStatus(eq(deliveryId), any(DeliveryStatus.class)))
                .thenReturn(response);

        // 2. Act & Assert
        mockMvc.perform(patch("/api/v1/deliveries/{id}/status", deliveryId)
                        // KEY FIX: Use statusEnum.name() to get the exact string value
                        .param("status", statusEnum.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(statusEnum.name()));
    }
}