package com.cts.eventsphere.exception.finance;

/**
 * Error thrown when a particular Budget is not found.
 *
 * @author 2480081
 * @version 1.0
 * @since 28-02-2026
 */
public class BudgetNotFoundException extends RuntimeException{
    public BudgetNotFoundException(String message){
        super(message);
    }
}
