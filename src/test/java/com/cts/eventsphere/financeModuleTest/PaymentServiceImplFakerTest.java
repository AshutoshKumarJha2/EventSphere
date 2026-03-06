package com.cts.eventsphere.financeModuleTest;

import com.cts.eventsphere.dto.mapper.payment.PaymentRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.payment.PaymentResponseDtoMapper;
import com.cts.eventsphere.dto.payment.PaymentRequestDto;
import com.cts.eventsphere.dto.payment.PaymentResponseDto;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;
import com.cts.eventsphere.model.Expense;
import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.model.Payment;
import com.cts.eventsphere.model.data.PaymentStatus;
import com.cts.eventsphere.repository.ExpenseRepository;
import com.cts.eventsphere.repository.PaymentRepository;
import com.cts.eventsphere.service.impl.PaymentServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplFakerTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private PaymentRequestDtoMapper paymentRequestDtoMapper;

    @Mock
    private PaymentResponseDtoMapper paymentResponseDtoMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void testMarkPaymentSuccess() {
        String expenseId = faker.internet().uuid();
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);

        PaymentRequestDto request = new PaymentRequestDto(
                new Invoice(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)),
                LocalDateTime.now()
        );

        Payment payment = new Payment();
        payment.setPaymentId(faker.internet().uuid());
        payment.setAmount(request.amount());
        payment.setInvoice(request.invoice());
        payment.setPaymentDate(request.date());
        payment.setStatus(PaymentStatus.completed);

        PaymentResponseDto response = new PaymentResponseDto(
                payment.getPaymentId(),
                payment.getInvoice(),
                payment.getAmount(),
                com.cts.eventsphere.model.data.PaymentMethod.credit_card,
                PaymentStatus.completed,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString()
        );

        Mockito.when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));
        Mockito.when(paymentRequestDtoMapper.toEntity(request)).thenReturn(payment);
        Mockito.when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        Mockito.when(paymentResponseDtoMapper.toDTO(payment)).thenReturn(response);

        PaymentResponseDto result = paymentService.markPayment(expenseId, request);

        assertNotNull(result);
        assertEquals(PaymentStatus.completed, result.status());
        assertEquals(request.amount(), result.amount());
    }

    @Test
    void testMarkPaymentExpenseNotFound() {
        String expenseId = faker.internet().uuid();
        PaymentRequestDto request = new PaymentRequestDto(
                new Invoice(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)),
                LocalDateTime.now()
        );

        Mockito.when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        assertThrows(ExpenseNotFoundException.class,
                () -> paymentService.markPayment(expenseId, request));
    }
}
