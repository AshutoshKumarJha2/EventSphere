package com.cts.eventsphere.dto.invoice;

import com.cts.eventsphere.model.data.InvoiceStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for capturing new invoice details from the finance officer.
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */
public record InvoiceRequestDto(
        @NotBlank(message = "Invoices must be associated with a Contract ID")
        String contractId,

        @NotNull(message = "Invoice amount is required")
        @PositiveOrZero(message = "Invoice amount cannot be negative")
        BigDecimal totalAmount,

        @NotNull(message = "Payment due date is required")
        @FutureOrPresent(message = "Due date cannot be in the past")
        LocalDateTime dueDate,

        @NotNull(message = "Initial invoice status is required")
        InvoiceStatus status
) {}
