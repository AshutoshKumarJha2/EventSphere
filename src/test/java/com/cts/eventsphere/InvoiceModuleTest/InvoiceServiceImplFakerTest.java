package com.cts.eventsphere.InvoiceModuleTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.eventsphere.service.impl.InvoiceServiceImpl;
import com.cts.eventsphere.repository.InvoiceRepository;

import com.cts.eventsphere.dto.mapper.invoice.InvoiceRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.invoice.InvoiceResponseDtoMapper;

import com.cts.eventsphere.exception.invoice.InvoiceNotFoundException;

import com.github.javafaker.Faker;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplFakerTest {
    @Mock private InvoiceRepository invoiceRepository;
    @Mock private InvoiceRequestDtoMapper requestMapper;
    @Mock private InvoiceResponseDtoMapper responseMapper;

    @InjectMocks private InvoiceServiceImpl invoiceService;
    private Faker faker = new Faker();

    @Test
    void testDeleteInvoice_NotFound() {
        String id = "non-existent";
        when(invoiceRepository.existsById(id)).thenReturn(false);
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.deleteInvoice(id));
    }
}

