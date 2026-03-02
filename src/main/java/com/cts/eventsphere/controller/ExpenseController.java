package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
import com.cts.eventsphere.dto.payment.PaymentRequestDto;
import com.cts.eventsphere.dto.payment.PaymentResponseDto;
import com.cts.eventsphere.model.data.ExpenseStatus;
import com.cts.eventsphere.service.ExpenseService;
import com.cts.eventsphere.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final PaymentService paymentService;


    /**
     * Get all Expenses
     */
    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses(){
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    /**
     * Get all expense for an event
     */
    @GetMapping("/events/{eventId}/expenses")
    public ResponseEntity<List<ExpenseResponseDto>> getEventExpenses(@PathVariable String eventId){
        return ResponseEntity.ok(expenseService.getExpenseByEvent(eventId));
    }

    /**
     * Log an Expense
     */
    @PostMapping("/events/{eventId}/expenses")
    public ResponseEntity<ExpenseResponseDto> createExpense(@PathVariable String eventId , @RequestBody ExpenseRequestDto request){
        return new ResponseEntity<>(expenseService.createExpense(eventId , request),HttpStatus.CREATED);
    }

    /**
     * Delete an Expense
     */
    @DeleteMapping("/expenses/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String expenseId){
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Approve or Reject Expense
     */
    @PatchMapping("expenses/{expenseId}/status")
    public ResponseEntity<ExpenseResponseDto> updateExpenseStatus(@PathVariable String expenseId , @RequestParam ExpenseStatus status){
        ExpenseResponseDto response = expenseService.updateExpenseStatus(expenseId , status);
        return ResponseEntity.ok(response);
    }

    /**
     * Mark Payment complete
     */
    @PostMapping("expenses/{expenseId}/payment")
    public ResponseEntity<PaymentResponseDto> makePayment(@PathVariable String expenseId , @RequestBody PaymentRequestDto request){
        PaymentResponseDto response = paymentService.markPayment(expenseId , request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}
