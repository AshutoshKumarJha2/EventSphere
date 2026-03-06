package com.cts.eventsphere.exception.finance;

import lombok.extern.slf4j.Slf4j;

/**
 * Error thrown when a payment is not found.
 *
 * @author 2480081
 * @version 1.0
 * @since 28-02-2026
 */
@Slf4j
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
        log.error("Payment Exception Triggered: {}", message);
    }
}
