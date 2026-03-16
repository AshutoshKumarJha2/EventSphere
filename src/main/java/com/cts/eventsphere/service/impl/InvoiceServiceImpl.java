package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.dto.mapper.invoice.InvoiceRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.invoice.InvoiceResponseDtoMapper;
import com.cts.eventsphere.exception.invoice.InvoiceNotFoundException;
import com.cts.eventsphere.exception.invoice.InvoicePdfGenerationException;
import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.repository.InvoiceRepository;
import com.cts.eventsphere.service.InvoiceService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service Implementation for Invoice operations.
 * Manages financial billing records and professional PDF document generation.
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceRequestDtoMapper requestDtoMapper;
    private final InvoiceResponseDtoMapper responseDtoMapper;

    /**
     * Creates a new invoice and persists it to the database.
     *
     * @param request the invoice details provided via DTO
     * @return the created invoice details as a response DTO
     */
    @Override
    @Transactional
    public InvoiceResponseDto createInvoice(InvoiceRequestDto request){
        log.info("Attempting to create invoice for contract ID: {}", request.contractId());
        Invoice saved = invoiceRepository.save(requestDtoMapper.toEntity(request));
        log.info("Successfully created invoice with ID: {}", saved.getInvoiceId());
        return responseDtoMapper.toDto(saved);
    }

    /**
     * Retrieves an invoice by its unique ID.
     *
     * @param invoiceId the unique identifier
     * @return the invoice response DTO
     * @throws InvoiceNotFoundException if ID not found
     */
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceById(String invoiceId){
        log.info("Fetching invoice details for ID: {}", invoiceId);
        return invoiceRepository.findById(invoiceId)
                .map(responseDtoMapper::toDto)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
    }

    /**
     * Fetches all invoices currently registered in the system.
     *
     * @return a list of invoice response DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getAllInvoices(){
        log.info("Fetching all invoices from database");
        return invoiceRepository.findAll().stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    /**
     * Generates an invoice specifically linked to a contract ID.
     *
     * @param contractId the linked contract identifier
     * @param dto the invoice details
     * @return the saved invoice response DTO
     */
    @Override
    @Transactional
    public InvoiceResponseDto generateInvoice(String contractId, InvoiceRequestDto dto) {
        Invoice invoice = requestDtoMapper.toEntity(dto);
        invoice.setContractId(contractId); // Linking the contract
        return responseDtoMapper.toDto(invoiceRepository.save(invoice));
    }

    /**
     * Generates a PDF byte array for a specific invoice.
     *
     * @param invoiceId the ID of the invoice to print
     * @return byte array containing PDF data
     * @throws InvoiceNotFoundException if invoice does not exist
     * @throws InvoicePdfGenerationException if PDF creation fails
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] generateInvoicePdf(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("EVENTSPHERE INVOICE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceId()));
            document.add(new Paragraph("Contract Reference: " + invoice.getContractId()));
            document.add(new Paragraph("Amount Due: $" + invoice.getTotalAmount()));
            document.add(new Paragraph("Generated on: " + LocalDateTime.now()));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new InvoicePdfGenerationException("PDF Error: " + e.getMessage());
        }
    }

    /**
     * Updates the comprehensive details of an existing invoice record.
     *
     * @param invoiceId the ID to update
     * @param request the updated invoice details
     * @return the updated invoice response
     */
    @Override
    @Transactional
    public InvoiceResponseDto updateInvoice(String invoiceId, InvoiceRequestDto request){
        log.info("Attempting to update invoice ID: {}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));

        invoice.setTotalAmount(request.totalAmount());
        invoice.setDueDate(request.dueDate());
        invoice.setStatus(request.status());

        log.info("Successfully updated invoice details for ID: {}", invoiceId);
        return responseDtoMapper.toDto(invoiceRepository.save(invoice));
    }

    /**
     * Removes an invoice record from the system.
     *
     * @param invoiceId the unique identifier to delete
     * @throws InvoiceNotFoundException if the invoice ID does not exist in the database
     */
    @Override
    @Transactional
    public void deleteInvoice(String invoiceId){
        log.info("Attempting to delete invoice ID: {}", invoiceId);

        if(!invoiceRepository.existsById(invoiceId)){
            throw new InvoiceNotFoundException(invoiceId);
        }

        invoiceRepository.deleteById(invoiceId);
        log.info("Successfully deleted invoice ID: {}", invoiceId);
    }
}
