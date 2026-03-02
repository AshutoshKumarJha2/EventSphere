package com.cts.eventsphere.dto.payment;

import com.cts.eventsphere.model.data.PaymentStatus;

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
        String invoiceId,
        BigDecimal amount,
        LocalDateTime date

) {
}
