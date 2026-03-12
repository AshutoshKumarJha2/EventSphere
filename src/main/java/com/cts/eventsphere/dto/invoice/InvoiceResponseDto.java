package com.cts.eventsphere.dto.invoice;

import com.cts.eventsphere.model.data.InvoiceStatus;
import jakarta.validation.constraints.*;

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
        @NotBlank(message = "Response Error: Invoice ID is missing")
        String invoiceId,

        @NotBlank(message = "Response Error: Contract ID is missing")
        String contractId,

        @NotNull(message = "Response Error: Total amount is missing")
        @PositiveOrZero(message = "Response Error: Amount cannot be negative")
        BigDecimal totalAmount,

        @NotNull(message = "Response Error: Due date is missing")
        LocalDateTime dueDate,

        @NotNull(message = "Response Error: Invoice status is missing")
        InvoiceStatus status,

        @NotNull(message = "Response Error: Created timestamp is missing")
        LocalDateTime createdAt,

        @NotNull(message = "Response Error: Updated timestamp is missing")
        LocalDateTime updatedAt
) {}
