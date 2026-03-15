package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
import com.cts.eventsphere.dto.payment.PaymentRequestDto;
import com.cts.eventsphere.dto.payment.PaymentResponseDto;
import com.cts.eventsphere.model.data.ExpenseStatus;
import com.cts.eventsphere.service.ExpenseService;
import com.cts.eventsphere.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing Expense operations in the EventSphere application.
 *
 * <p>This REST controller provides endpoints to create, retrieve, update,
 * and delete expenses, as well as process related payments. It delegates
 * business logic to {@link ExpenseService} and {@link PaymentService} and
 * enforces authorization rules using {@code @PreAuthorize} annotations.</p>
 *
 * <p>Only users with specific roles (ADMIN, ORGANIZER, FINANCE_MANAGER)
 * are permitted to perform expense and payment operations.</p>
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor

public class ExpenseController {

    private final ExpenseService expenseService;
    private final PaymentService paymentService;


    /**
     * Retrieves all expenses in the system.
     *
     * @return a ResponseEntity containing a list of {@link ExpenseResponseDto}
     *         objects and HTTP status 200 (OK)
     */
    @GetMapping("/expenses")
    @PreAuthorize("hasAnyRole('ADMIN','FINANCE_MANAGER')")
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses(){
        log.info("Request to fetch all expenses");
        List<ExpenseResponseDto> response = expenseService.getAllExpenses();
        log.info("Response for all expenses: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all expenses for a specific event.
     *
     * @param eventId the unique identifier of the event
     * @return a ResponseEntity containing a list of {@link ExpenseResponseDto}
     *         objects for the given event and HTTP status 200 (OK)
     */
    @GetMapping("/events/{eventId}/expenses")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'FINANCE_MANAGER')")
    public ResponseEntity<List<ExpenseResponseDto>> getEventExpenses(@PathVariable String eventId ){
        log.info("Request to fetch expenses for eventId: {} ", eventId);
        List<ExpenseResponseDto> response = expenseService.getExpenseByEvent(eventId );
        log.info("Response for eventId {}: {} ", eventId, response);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new expense for a specific event.
     *
     * @param eventId the unique identifier of the event
     * @param request the validated expense request payload
     * @return a ResponseEntity containing the created {@link ExpenseResponseDto}
     *         and HTTP status 201 (CREATED)
     */
    @PostMapping("/events/{eventId}/expenses")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'FINANCE_MANAGER')")
    public ResponseEntity<ExpenseResponseDto> createExpense(@PathVariable String eventId ,@Valid @RequestBody ExpenseRequestDto request){
        log.info("Request to create expense for eventId: {} with data: {}", eventId, request);
        ExpenseResponseDto response = expenseService.createExpense(eventId , request);
        log.info("Response for created expense in eventId {}: {}", eventId, response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Deletes an expense by its ID.
     *
     * @param expenseId the unique identifier of the expense
     * @return a ResponseEntity with HTTP status 204 (NO_CONTENT) if deletion is successful
     */
    @DeleteMapping("/expenses/{expenseId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<Void> deleteExpense(@PathVariable String expenseId){
        log.info("Request to delete expense with ID: {}", expenseId);
        expenseService.deleteExpense(expenseId);
        log.info("Successfully deleted expense with ID: {}", expenseId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the status of an expense (approve or reject).
     *
     * @param expenseId the unique identifier of the expense
     * @param status the new status to be applied (e.g., APPROVED, REJECTED)
     * @return a ResponseEntity containing the updated {@link ExpenseResponseDto}
     *         and HTTP status 200 (OK)
     */
    @PatchMapping("expenses/{expenseId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','FINANCE_MANAGER')")
    public ResponseEntity<ExpenseResponseDto> updateExpenseStatus(@PathVariable String expenseId , @RequestParam ExpenseStatus status){
        log.info("Request to update status for expenseId: {} to status: {}", expenseId, status);
        ExpenseResponseDto response = expenseService.updateExpenseStatus(expenseId , status);
        log.info("Response for updated expense status: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Marks a payment as complete for a specific expense.
     *
     * @param expenseId the unique identifier of the expense
     * @param request the validated payment request payload
     * @return a ResponseEntity containing the processed {@link PaymentResponseDto}
     *         and HTTP status 201 (CREATED)
     */
    @PostMapping("expenses/{expenseId}/payment")
    @PreAuthorize("hasAnyRole('ADMIN','FINANCE_MANAGER')")
    public ResponseEntity<PaymentResponseDto> makePayment(@PathVariable String expenseId ,@Valid @RequestBody PaymentRequestDto request){
        log.info("Request to process payment for expenseId: {} with data: {}", expenseId, request);
        PaymentResponseDto response = paymentService.markPayment(expenseId , request);
        log.info("Response for processed payment: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}
