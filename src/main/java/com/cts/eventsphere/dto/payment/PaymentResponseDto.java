package com.cts.eventsphere.dto.payment;

import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.model.data.PaymentMethod;
import com.cts.eventsphere.model.data.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Payment Response Dto representing payment transaction details
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
@Builder
public record PaymentResponseDto(
    String paymentId,
    Invoice invoice,
    BigDecimal amount,
    PaymentMethod method,
    PaymentStatus status,
    String paymentDate,
    String createdAt,
    String updatedAt
) {
}
