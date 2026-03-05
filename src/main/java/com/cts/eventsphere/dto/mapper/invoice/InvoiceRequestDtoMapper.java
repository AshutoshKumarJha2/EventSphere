package com.cts.eventsphere.dto.mapper.invoice;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceRequestDtoMapper {
    public Invoice toEntity(InvoiceRequestDto dto){
        Invoice invoice = new Invoice();
        invoice.setContractId(dto.contractId());
        invoice.setTotalAmount(dto.totalAmount());
        invoice.setDueDate(dto.dueDate());
        invoice.setStatus(dto.status());
        return invoice;
    }
}
