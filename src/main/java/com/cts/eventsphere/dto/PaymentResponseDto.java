package com.cts.eventsphere.dto;

import com.cts.eventsphere.model.data.PaymentMethod;
import com.cts.eventsphere.model.data.PaymentStatus;

import java.math.BigDecimal;

/**
 * [Detailed description of the class's responsibility]
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
public record PaymentResponseDto(
    String paymentId,
    String invoiceId,
    BigDecimal amount,
    PaymentMethod method,
    PaymentStatus status,
    String paymentDate,
    String createdAt,
    String updatedAt
) {
}
