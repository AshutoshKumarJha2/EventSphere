package com.cts.eventsphere.dto.mapper.invoice;

import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.model.Invoice;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Invoice entity to InvoiceResponseDto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

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
