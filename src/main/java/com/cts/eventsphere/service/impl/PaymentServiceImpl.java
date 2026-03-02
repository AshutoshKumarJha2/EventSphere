package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.payment.PaymentRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.payment.PaymentResponseDtoMapper;
import com.cts.eventsphere.dto.payment.PaymentRequestDto;
import com.cts.eventsphere.dto.payment.PaymentResponseDto;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;
import com.cts.eventsphere.model.Expense;
import com.cts.eventsphere.model.Payment;
import com.cts.eventsphere.model.data.PaymentStatus;
import com.cts.eventsphere.repository.ExpenseRepository;
import com.cts.eventsphere.repository.PaymentRepository;
import com.cts.eventsphere.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of Payment Service
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final PaymentRequestDtoMapper paymentRequestDtoMapper;
    private final PaymentResponseDtoMapper paymentResponseDtoMapper;

    /**
     * Marks payment as completed for an approved expense
     */

    @Override
    public PaymentResponseDto markPayment(String expenseId , PaymentRequestDto request) throws ExpenseNotFoundException{
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));
        Payment payment = paymentRequestDtoMapper.toEntity(request);
        payment.setStatus(PaymentStatus.completed);
        return paymentResponseDtoMapper.toDTO(paymentRepository.save(payment));
    }
}
