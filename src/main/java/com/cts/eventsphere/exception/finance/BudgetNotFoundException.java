package com.cts.eventsphere.exception.finance;

import lombok.extern.slf4j.Slf4j;

/**
 * Error thrown when a particular Budget is not found.
 *
 * @author 2480081
 * @version 1.0
 * @since 28-02-2026
 */
@Slf4j
public class BudgetNotFoundException extends RuntimeException {
    public BudgetNotFoundException(String message) {
        super(message);
        log.error("Budget Exception Triggered: {}", message);
    }
}
