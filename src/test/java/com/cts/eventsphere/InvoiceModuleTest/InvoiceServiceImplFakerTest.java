package com.cts.eventsphere.InvoiceModuleTest;

import com.cts.eventsphere.controller.InvoiceController;
import com.cts.eventsphere.exception.invoice.InvoiceNotFoundException;
import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.repository.InvoiceRepository;
import com.cts.eventsphere.service.InvoiceService;
import com.cts.eventsphere.service.impl.InvoiceServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
// JUnit 5 Assertions
import static org.junit.jupiter.api.Assertions.*;

// Mockito Imports
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;

// Spring Web & HTTP Imports (Crucial for PDF Headers)
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition; // If using ContentDisposition object
import org.springframework.http.HttpHeaders;

// Java Faker & Utilities
import com.github.javafaker.Faker;

import java.util.Optional;
import java.util.UUID;

// Your Project Specifics (Adjust package names)
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.service.InvoiceService;
import com.cts.eventsphere.controller.InvoiceController;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplFakerTest {

    @Mock private InvoiceRepository invoiceRepository;
    @InjectMocks private InvoiceServiceImpl invoiceService;
    private Faker faker = new Faker();

    @Test
    void generateInvoicePdf_whenInvoiceExists_shouldReturnByteArray() {
        // Arrange
        String id = faker.internet().uuid();
        Invoice mockInvoice = new Invoice();
        mockInvoice.setInvoiceId(id);

        when(invoiceRepository.findById(id)).thenReturn(Optional.of(mockInvoice));

        // Act
        byte[] result = invoiceService.generateInvoicePdf(id);

        // Assert
        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(invoiceRepository).findById(id);
    }

    @Test
    void generateInvoicePdf_whenNotFound_throwsException() {
        String id = faker.internet().uuid();
        when(invoiceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.generateInvoicePdf(id));
    }
}
