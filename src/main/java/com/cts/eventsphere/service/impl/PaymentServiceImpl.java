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
import com.cts.eventsphere.service.NotificationService;
import com.cts.eventsphere.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation for Service Interface for Payment Class.
 * Provides business logic for processing payments related to approved expenses.
 * This service manages the transition of payment statuses and triggers
 * notifications via {@link NotificationService} to confirm successful
 * financial transactions to event organizers.
 *
 * @author 2480081
 * @version 1.1
 * @since 01-03-2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final PaymentRequestDtoMapper paymentRequestDtoMapper;
    private final PaymentResponseDtoMapper paymentResponseDtoMapper;
    private final NotificationService notificationService;

    /**
     * Marks a payment as completed for a specific expense and triggers a confirmation notification.
     *
     * @param expenseId the unique identifier of the expense being paid
     * @param request the DTO containing payment details such as transaction ID or method
     * @return the response DTO representing the processed payment
     * @throws ExpenseNotFoundException if no expense exists with the given ID
     */
    @Override
    public PaymentResponseDto markPayment(String expenseId , PaymentRequestDto request) throws ExpenseNotFoundException{
        log.info("Request to mark payment for expenseId: {} with details: {}", expenseId, request);
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));
        Payment payment = paymentRequestDtoMapper.toEntity(request);
        payment.setStatus(PaymentStatus.completed);
        PaymentResponseDto response = paymentResponseDtoMapper.toDTO(paymentRepository.save(payment));
        log.info("Payment successfully processed for expenseId: {}. Status: {}", expenseId, response.status());
        notificationService.sendNotification(
                expense.getEvent().getEventId(),
                expense.getEvent().getOrganizerId(),
                "Payment Finalized: " + expense.getDescription() +
                        " | Amount: " + request.amount() +
                        " | Date: " + request.date() +
                        " | Status: " + PaymentStatus.completed,
                "PAYMENT_SUCCESS"
        );
        return response;
    }
}
