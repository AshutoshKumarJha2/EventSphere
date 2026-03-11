package com.cts.eventsphere.dto.mapper.payment;

import com.cts.eventsphere.dto.payment.PaymentResponseDto;
import com.cts.eventsphere.model.Payment;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Payment Entity to payment response dto
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */

@Component
public class PaymentResponseDtoMapper {
    public PaymentResponseDto toDTO(Payment payment) {
//        return new PaymentResponseDto(
//                payment.getPaymentId(),
//                payment.getInvoice(),
//                payment.getAmount(),
//                payment.getMethod(),
//                payment.getStatus(),
//                payment.getPaymentDate().toString(),
//                payment.getCreatedAt().toString(),
//                payment.getUpdatedAt().toString()
//        );

        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .invoice(payment.getInvoice())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : null)
                .createdAt(payment.getCreatedAt() != null ? payment.getCreatedAt().toString() : null)
                .updatedAt(payment.getUpdatedAt() != null ? payment.getUpdatedAt().toString() : null)
                .build();
    }
}
