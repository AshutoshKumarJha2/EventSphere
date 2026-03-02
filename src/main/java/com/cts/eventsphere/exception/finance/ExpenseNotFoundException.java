package com.cts.eventsphere.exception.finance;

/**
 * Error thrown when an Expense is not found.
 *
 * @author 2480081
 * @version 1.0
 * @since 28-02-2026
 */
public class ExpenseNotFoundException extends RuntimeException{
    public ExpenseNotFoundException(String message){
        super(message);
    }
}
