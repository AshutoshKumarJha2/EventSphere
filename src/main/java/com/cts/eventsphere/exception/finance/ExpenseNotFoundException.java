package com.cts.eventsphere.exception.finance;

import lombok.extern.slf4j.Slf4j;

/**
 * Error thrown when an Expense is not found.
 *
 * @author 2480081
 * @version 1.0
 * @since 28-02-2026
 */
@Slf4j
public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException(String expenseId) {
        super("Expense not found with ID: " + expenseId);
        log.error("CRITICAL: Attempted to access non-existent expense: {}", expenseId);
    }
}
