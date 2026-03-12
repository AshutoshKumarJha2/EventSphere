package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Invoice Entity
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Slf4j
@Validated
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public @Valid InvoiceResponseDto create(@Valid @RequestBody InvoiceRequestDto request) {
        log.info("Received request to create invoice for contractId={}", request.contractId());
        InvoiceResponseDto response = invoiceService.createInvoice(request);
        log.info("Invoice created successfully with ID={}", response.invoiceId());
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FINANCE','VENDOR','ADMIN')")
    public @Valid InvoiceResponseDto getById(@PathVariable String id) {
        log.info("Fetching invoice with ID={}", id);
        return invoiceService.getInvoiceById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FINANCE','VENDOR','ADMIN')")
    public List< @Valid InvoiceResponseDto> getAll() {
        log.info("Fetching all invoices");
        return invoiceService.getAllInvoices();
    }

    @PutMapping("/{id}")
    public @Valid InvoiceResponseDto update(
            @PathVariable String id,
            @Valid @RequestBody InvoiceRequestDto request) {

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