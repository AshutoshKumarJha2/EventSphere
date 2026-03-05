package com.cts.eventsphere.dto.invoice;

import com.cts.eventsphere.model.data.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceRequestDto(
        String contractId,
        BigDecimal totalAmount,
        LocalDateTime dueDate,
        InvoiceStatus status
) {
}
