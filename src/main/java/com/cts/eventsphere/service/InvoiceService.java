package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;

import java.util.List;

public interface InvoiceService {
    InvoiceResponseDto createInvoice(InvoiceRequestDto request);

    InvoiceResponseDto getInvoiceById(String invoiceId);

    List<InvoiceResponseDto> getAllInvoices();

    InvoiceResponseDto updateInvoice(String invoiceId, InvoiceRequestDto request);

    void deleteInvoice(String invoiceId);
}
