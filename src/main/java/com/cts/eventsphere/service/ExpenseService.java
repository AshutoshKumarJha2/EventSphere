package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;
import com.cts.eventsphere.model.data.ExpenseStatus;

import java.util.List;

/**
 * Service interface for Expense Operations
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
public interface ExpenseService {
    ExpenseResponseDto createExpense(String eventId , ExpenseRequestDto request) throws EventNotFoundException;

    List<ExpenseResponseDto> getExpenseByEvent(String eventId);

    List<ExpenseResponseDto> getAllExpenses() throws EventNotFoundException;

    void deleteExpense(String expenseId) throws ExpenseNotFoundException;

    ExpenseResponseDto updateExpenseStatus(String expenseId, ExpenseStatus status) throws ExpenseNotFoundException;
}
