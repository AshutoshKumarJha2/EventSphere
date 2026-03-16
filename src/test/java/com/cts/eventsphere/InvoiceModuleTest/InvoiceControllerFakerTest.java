package com.cts.eventsphere.InvoiceModuleTest;

import com.cts.eventsphere.controller.InvoiceController;
import com.cts.eventsphere.service.InvoiceService;
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
import java.util.UUID;

// Your Project Specifics (Adjust package names)
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.service.InvoiceService;
import com.cts.eventsphere.controller.InvoiceController;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceControllerFakerTest {

    @Mock
    private InvoiceService invoiceService;
    @InjectMocks
    private InvoiceController invoiceController;
    private Faker faker = new Faker();

    @Test
    void downloadInvoice_shouldReturnPdfWithCorrectHeaders() {
        // Arrange
        String invoiceId = faker.internet().uuid();
        byte[] mockPdfContent = "Fake PDF Binary Content".getBytes();

        when(invoiceService.generateInvoicePdf(invoiceId)).thenReturn(mockPdfContent);

        // Act
        ResponseEntity<byte[]> response = invoiceController.downloadPdf(invoiceId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());

        // Check if the filename is set correctly in headers
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        assertTrue(contentDisposition.contains("attachment"));
        assertTrue(contentDisposition.contains("invoice_" + invoiceId + ".pdf"));

        assertArrayEquals(mockPdfContent, response.getBody());
        verify(invoiceService, times(1)).generateInvoicePdf(invoiceId);
    }
}
