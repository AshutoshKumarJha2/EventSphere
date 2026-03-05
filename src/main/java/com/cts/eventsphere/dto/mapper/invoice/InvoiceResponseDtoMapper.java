package com.cts.eventsphere.dto.mapper.invoice;

import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceResponseDtoMapper {
    public InvoiceResponseDto toDto(Invoice invoice){
        return new InvoiceResponseDto(
                invoice.getInvoiceId(),
                invoice.getContractId(),
                invoice.getTotalAmount(),
                invoice.getDueDate(),
                invoice.getStatus(),
                invoice.getCreatedAt(),
                invoice.getUpdatedAt()
        );
    }
}
