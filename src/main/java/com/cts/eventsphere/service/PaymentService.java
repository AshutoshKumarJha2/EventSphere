package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.payment.PaymentRequestDto;
import com.cts.eventsphere.dto.payment.PaymentResponseDto;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;

/**
 * Service interface for Payment Operations
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
public interface PaymentService {
    PaymentResponseDto markPayment(String expenseId , PaymentRequestDto request) throws ExpenseNotFoundException;
}
