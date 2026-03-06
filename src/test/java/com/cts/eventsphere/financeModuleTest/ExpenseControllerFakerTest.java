package com.cts.eventsphere.financeModuleTest;

import com.cts.eventsphere.controller.ExpenseController;
import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
import com.cts.eventsphere.dto.payment.PaymentRequestDto;
import com.cts.eventsphere.dto.payment.PaymentResponseDto;
import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.model.User;
import com.cts.eventsphere.model.data.ExpenseStatus;
import com.cts.eventsphere.service.ExpenseService;
import com.cts.eventsphere.service.PaymentService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ExpenseControllerFakerTest{

    @Mock
    private ExpenseService expenseService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private ExpenseController expenseController;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void testGetAllExpenses() {
        ExpenseResponseDto response = new ExpenseResponseDto(
                faker.internet().uuid(),
                faker.internet().uuid(),
                faker.commerce().productName(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)),
                LocalDate.now().toString(),
                new User(),
                ExpenseStatus.approved,
                LocalDate.now().toString(),
                LocalDate.now().toString()
        );

        Mockito.when(expenseService.getAllExpenses()).thenReturn(List.of(response));

        ResponseEntity<List<ExpenseResponseDto>> result = expenseController.getAllExpenses();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
        assertEquals(response.description(), result.getBody().get(0).description());
    }

    @Test
    void testGetEventExpenses() {
        String eventId = faker.internet().uuid();
        ExpenseResponseDto response = new ExpenseResponseDto(
                faker.internet().uuid(),
                eventId,
                faker.commerce().productName(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)),
                LocalDate.now().toString(),
                null,
                ExpenseStatus.submitted,
                LocalDate.now().toString(),
                LocalDate.now().toString()
        );

        Mockito.when(expenseService.getExpenseByEvent(eventId)).thenReturn(List.of(response));

        ResponseEntity<List<ExpenseResponseDto>> result = expenseController.getEventExpenses(eventId);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(eventId, result.getBody().get(0).eventId());
    }

    @Test
    void testCreateExpense() {
        String eventId = faker.internet().uuid();
        ExpenseRequestDto request = new ExpenseRequestDto(
                faker.commerce().productName(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)),
                LocalDate.now()
        );

        ExpenseResponseDto response = new ExpenseResponseDto(
                faker.internet().uuid(),
                eventId,
                request.description(),
                request.amount(),
                request.date().toString(),
                null,
                ExpenseStatus.submitted,
                LocalDate.now().toString(),
                LocalDate.now().toString()
        );

        Mockito.when(expenseService.createExpense(eq(eventId), any(ExpenseRequestDto.class)))
                .thenReturn(response);

        ResponseEntity<ExpenseResponseDto> result = expenseController.createExpense(eventId, request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(request.description(), result.getBody().description());
    }

    @Test
    void testDeleteExpense() {
        String expenseId = faker.internet().uuid();

        ResponseEntity<Void> result = expenseController.deleteExpense(expenseId);

        assertEquals(204, result.getStatusCode().value());
        Mockito.verify(expenseService, Mockito.times(1)).deleteExpense(expenseId);
    }

    @Test
    void testUpdateExpenseStatus() {
        String expenseId = faker.internet().uuid();
        ExpenseResponseDto response = new ExpenseResponseDto(
                expenseId,
                faker.internet().uuid(),
                faker.commerce().productName(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)),
                LocalDate.now().toString(),
                null,
                ExpenseStatus.approved,
                LocalDate.now().toString(),
                LocalDate.now().toString()
        );

        Mockito.when(expenseService.updateExpenseStatus(eq(expenseId), eq(ExpenseStatus.approved)))
                .thenReturn(response);

        ResponseEntity<ExpenseResponseDto> result = expenseController.updateExpenseStatus(expenseId, ExpenseStatus.approved);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(ExpenseStatus.approved, result.getBody().status());
    }

    @Test
    void testMakePayment() {
        String expenseId = faker.internet().uuid();

        PaymentRequestDto request = new PaymentRequestDto(
                new Invoice(), // assuming Invoice is a simple object for test
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)),
                LocalDateTime.now()
        );

        PaymentResponseDto response = new PaymentResponseDto(
                faker.internet().uuid(),
                request.invoice(),
                request.amount(),
                com.cts.eventsphere.model.data.PaymentMethod.credit_card,
                com.cts.eventsphere.model.data.PaymentStatus.completed,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString()
        );

        Mockito.when(paymentService.markPayment(eq(expenseId), any(PaymentRequestDto.class)))
                .thenReturn(response);

        ResponseEntity<PaymentResponseDto> result = expenseController.makePayment(expenseId, request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(com.cts.eventsphere.model.data.PaymentStatus.completed, result.getBody().status());
        assertEquals(request.amount(), result.getBody().amount());
    }

}

