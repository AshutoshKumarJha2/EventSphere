package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
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
    ExpenseResponseDto createExpense(String eventId , ExpenseRequestDto request);

    List<ExpenseResponseDto> getExpenseByEvent(String eventId);

    List<ExpenseResponseDto> getAllExpenses();

    void deleteExpense(String expenseId);

    ExpenseResponseDto updateExpenseStatus(String expenseId, ExpenseStatus status);
}
