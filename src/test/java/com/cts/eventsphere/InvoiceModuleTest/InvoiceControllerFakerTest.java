package com.cts.eventsphere.InvoiceModuleTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.cts.eventsphere.controller.InvoiceController;
import com.cts.eventsphere.service.InvoiceService;
import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.model.data.InvoiceStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class InvoiceControllerFakerTest {
    private MockMvc mockMvc;
    private Faker faker = new Faker();
    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock private InvoiceService invoiceService;
    @InjectMocks private InvoiceController invoiceController;

    @BeforeEach
    void setup() { mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build(); }

    @Test
    void createInvoice_ShouldReturnCreated() throws Exception {
        InvoiceRequestDto request = new InvoiceRequestDto(
                faker.internet().uuid(), BigDecimal.valueOf(1200.50),
                LocalDateTime.now().plusDays(30), InvoiceStatus.issued
        );

        InvoiceResponseDto response = new InvoiceResponseDto(
                "INV-99", request.contractId(), request.totalAmount(),
                request.dueDate(), request.status(), LocalDateTime.now(), LocalDateTime.now()
        );

        when(invoiceService.createInvoice(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceId").value("INV-99"));
    }
}
