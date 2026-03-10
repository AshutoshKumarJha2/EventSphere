package com.cts.eventsphere.dto.payment;

import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.model.data.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * [Detailed description of the class's responsibility]
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
public record PaymentRequestDto(
        @NotBlank(message = "Invoice Id is required")
        Invoice invoice,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0",inclusive = false,message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "Payment date is required")
        LocalDateTime date

) {
}
