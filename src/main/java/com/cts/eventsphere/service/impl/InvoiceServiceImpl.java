package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.dto.mapper.invoice.InvoiceRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.invoice.InvoiceResponseDtoMapper;
import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.repository.InvoiceRepository;
import com.cts.eventsphere.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceRequestDtoMapper requestDtoMapper;
    private final InvoiceResponseDtoMapper responseDtoMapper;

    @Override
    public InvoiceResponseDto createInvoice(InvoiceRequestDto request){
        Invoice invoice = requestDtoMapper.toEntity(request);
        Invoice saved = invoiceRepository.save(invoice);
        return responseDtoMapper.toDto(saved);
    }

    @Override
    public InvoiceResponseDto getInvoiceById(String invoiceId){
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return responseDtoMapper.toDto(invoice);
    }

    @Override
    public List<InvoiceResponseDto> getAllInvoices(){
        return invoiceRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    @Override
    public InvoiceResponseDto updateInvoice(String invoiceId, InvoiceRequestDto request){
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setContractId(request.contractId());
        invoice.setTotalAmount(request.totalAmount());
        invoice.setDueDate(request.dueDate());
        invoice.setStatus(request.status());
        Invoice updated = invoiceRepository.save(invoice);
        return responseDtoMapper.toDto(updated);
    }

    @Override
    public void deleteInvoice(String invoiceId){
        if(!invoiceRepository.existsById(invoiceId)){
            throw new RuntimeException("Invoice not found");
        }
        invoiceRepository.deleteById(invoiceId);
    }
}
