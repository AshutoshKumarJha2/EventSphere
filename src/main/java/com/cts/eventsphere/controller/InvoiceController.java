package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Invoice Operations
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public InvoiceResponseDto create(@RequestBody InvoiceRequestDto request) {
        log.info("Received request to create invoice for contractId={}", request.contractId());
        InvoiceResponseDto response = invoiceService.createInvoice(request);
        log.info("Invoice created successfully with ID={}", response.invoiceId());
        return response;
    }

    @GetMapping("/{id}")
    public InvoiceResponseDto getById(@PathVariable String id) {
        log.info("Fetching invoice with ID={}", id);
        return invoiceService.getInvoiceById(id);
    }

    @GetMapping
    public List<InvoiceResponseDto> getAll() {
        log.info("Fetching all invoices");
        return invoiceService.getAllInvoices();
    }

    @PutMapping("/{id}")
    public InvoiceResponseDto update(
            @PathVariable String id,
            @RequestBody InvoiceRequestDto request) {

        log.info("Updating invoice with ID={}", id);
        InvoiceResponseDto response = invoiceService.updateInvoice(id, request);
        log.info("Invoice updated successfully with ID={}", id);
        return response;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.warn("Request to delete invoice with ID={}", id);
        invoiceService.deleteInvoice(id);
        log.info("Invoice deleted successfully with ID={}", id);
    }
}