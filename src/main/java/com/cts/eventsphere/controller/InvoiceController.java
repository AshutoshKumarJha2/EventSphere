package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public InvoiceResponseDto create(@RequestBody InvoiceRequestDto request) {
        log.info("Creating invoice");
        return invoiceService.createInvoice(request);
    }

    @GetMapping("/{id}")
    public InvoiceResponseDto getById(@PathVariable String id) {
        log.info("Fetching invoice with id: {}", id);
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

        log.info("Updating invoice with id: {}", id);
        return invoiceService.updateInvoice(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.info("Deleting invoice with id: {}", id);
        invoiceService.deleteInvoice(id);
    }
}
