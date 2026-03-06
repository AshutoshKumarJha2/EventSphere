package com.cts.eventsphere.dto.mapper.invoice;

import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.model.Invoice;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Invoice entity to InvoiceRequestDto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

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
