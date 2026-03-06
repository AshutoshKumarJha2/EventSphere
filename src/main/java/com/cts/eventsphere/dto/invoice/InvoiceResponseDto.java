package com.cts.eventsphere.dto.invoice;

import com.cts.eventsphere.model.data.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response Dto for Invoice Dto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

public record InvoiceResponseDto(
        String invoiceId,
        String contractId,
        BigDecimal totalAmount,
        LocalDateTime dueDate,
        InvoiceStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
