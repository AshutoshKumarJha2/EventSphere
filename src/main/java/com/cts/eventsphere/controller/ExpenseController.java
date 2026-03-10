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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * Controller for Expense Operation
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
     * Get all Expenses
     */
    @GetMapping("/expenses")
    @PreAuthorize("hasAnyRole('admin','finance_manager')")
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses(){
        log.info("Request to fetch all expenses");
        List<ExpenseResponseDto> response = expenseService.getAllExpenses();
        log.info("Response for all expenses: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all expense for an event
     */
    @GetMapping("/events/{eventId}/expenses")
    @PreAuthorize("hasAnyRole('admin', 'organizer', 'finance_manager')")
    public ResponseEntity<List<ExpenseResponseDto>> getEventExpenses(@PathVariable String eventId){
        log.info("Request to fetch expenses for eventId: {}", eventId);
        List<ExpenseResponseDto> response = expenseService.getExpenseByEvent(eventId);
        log.info("Response for eventId {}: {}", eventId, response);
        return ResponseEntity.ok(response);
    }

    /**
     * Log an Expense
     */
    @PostMapping("/events/{eventId}/expenses")
    @PreAuthorize("hasAnyRole('admin', 'organizer', 'finance_manager')")
    public ResponseEntity<ExpenseResponseDto> createExpense(@PathVariable String eventId ,@Valid @RequestBody ExpenseRequestDto request){
        log.info("Request to create expense for eventId: {} with data: {}", eventId, request);
        ExpenseResponseDto response = expenseService.createExpense(eventId , request);
        log.info("Response for created expense in eventId {}: {}", eventId, response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Delete an Expense
     */
    @DeleteMapping("/expenses/{expenseId}")
    @PreAuthorize("hasAnyRole('admin','organizer')")
    public ResponseEntity<Void> deleteExpense(@PathVariable String expenseId){
        log.info("Request to delete expense with ID: {}", expenseId);
        expenseService.deleteExpense(expenseId);
        log.info("Successfully deleted expense with ID: {}", expenseId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Approve or Reject Expense
     */
    @PatchMapping("expenses/{expenseId}/status")
    @PreAuthorize("hasAnyRole('admin','finance_manager')")
    public ResponseEntity<ExpenseResponseDto> updateExpenseStatus(@PathVariable String expenseId , @RequestParam ExpenseStatus status){
        log.info("Request to update status for expenseId: {} to status: {}", expenseId, status);
        ExpenseResponseDto response = expenseService.updateExpenseStatus(expenseId , status);
        log.info("Response for updated expense status: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Mark Payment complete
     */
    @PostMapping("expenses/{expenseId}/payment")
    @PreAuthorize("hasAnyRole('admin','finance_manager')")
    public ResponseEntity<PaymentResponseDto> makePayment(@PathVariable String expenseId ,@Valid @RequestBody PaymentRequestDto request){
        log.info("Request to process payment for expenseId: {} with data: {}", expenseId, request);
        PaymentResponseDto response = paymentService.markPayment(expenseId , request);
        log.info("Response for processed payment: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}
