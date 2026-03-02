package com.cts.eventsphere.dto.mapper.payment;

import com.cts.eventsphere.dto.payment.PaymentRequestDto;
import com.cts.eventsphere.model.Expense;
import com.cts.eventsphere.model.Payment;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert PaymentRequestDto to Payment Entity
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@Component
public class PaymentRequestDtoMapper {
    public Payment toEntity(PaymentRequestDto dto){
        Payment payment = new Payment();
        payment.setInvoiceId(dto.invoiceId());
        payment.setAmount(dto.amount());
        payment.setPaymentDate(dto.date());
        return payment;
    }

}
