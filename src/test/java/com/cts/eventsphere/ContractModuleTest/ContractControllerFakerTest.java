package com.cts.eventsphere.ContractModuleTest;

import com.cts.eventsphere.controller.ContractController;
import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.model.data.ContractStatus;
import com.cts.eventsphere.service.ContractService;
import com.github.javafaker.Faker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ContractControllerFakerTest {

    private MockMvc mockMvc;
    private final Faker faker = new Faker();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractController contractController;

    @BeforeEach
    void setup() {
        // Standalone setup for testing just the controller
        mockMvc = MockMvcBuilders.standaloneSetup(contractController).build();
    }

    @Test
    void createContract_ShouldReturn200() throws Exception {
        // 1. Ensure dates are clearly in the future to satisfy @Future constraints
        LocalDateTime futureStart = LocalDateTime.now().plusDays(1);
        LocalDateTime futureEnd = LocalDateTime.now().plusDays(10);

        // 2. Use data that satisfies common @Size(min=...) or @Pattern constraints
        ContractRequestDto request = new ContractRequestDto(
                "VENDOR-101",
                "EVENT-202",
                futureStart,
                futureEnd,
                BigDecimal.valueOf(5000),
                ContractStatus.active
        );

        ContractResponseDto response = new ContractResponseDto(
                "CON-123",
                request.vendorId(),
                request.eventId(),
                request.startDate(),
                request.endDate(),
                request.value(),
                request.status(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(contractService.createContract(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractId").value("CON-123"));
    }
}