package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.dto.mapper.invoice.InvoiceRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.invoice.InvoiceResponseDtoMapper;
import com.cts.eventsphere.exception.invoice.InvoiceNotFoundException;
import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.repository.InvoiceRepository;
import com.cts.eventsphere.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for Invoice Service
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

    @Override
    public InvoiceResponseDto createInvoice(InvoiceRequestDto request){
        log.info("Creating invoice...");
        Invoice invoice = requestDtoMapper.toEntity(request);
        Invoice saved = invoiceRepository.save(invoice);
        log.info("Invoice created with ID={}", saved.getInvoiceId());
        return responseDtoMapper.toDto(saved);
    }

    @Override
    public InvoiceResponseDto getInvoiceById(String invoiceId){
        log.info("Fetching invoice ID={}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));

        return responseDtoMapper.toDto(invoice);
    }

    @Override
    public List<InvoiceResponseDto> getAllInvoices(){
        log.info("Fetching all invoices");
        return invoiceRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    @Override
    public InvoiceResponseDto updateInvoice(String invoiceId, InvoiceRequestDto request){
        log.info("Updating invoice ID={}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));

        invoice.setContractId(request.contractId());
        invoice.setTotalAmount(request.totalAmount());
        invoice.setDueDate(request.dueDate());
        invoice.setStatus(request.status());

        Invoice updated = invoiceRepository.save(invoice);
        return responseDtoMapper.toDto(updated);
    }

    @Override
    public void deleteInvoice(String invoiceId){
        log.warn("Deleting invoice ID={}", invoiceId);

        if(!invoiceRepository.existsById(invoiceId)){
            throw new InvoiceNotFoundException(invoiceId);
        }

        invoiceRepository.deleteById(invoiceId);
    }
}
