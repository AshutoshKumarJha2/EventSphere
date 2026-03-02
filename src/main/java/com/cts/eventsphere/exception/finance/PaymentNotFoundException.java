package com.cts.eventsphere.exception.finance;

/**
 * Error thrown when a payment is not found.
 *
 * @author 2480081
 * @version 1.0
 * @since 28-02-2026
 */
public class PaymentNotFoundException extends RuntimeException{
    public PaymentNotFoundException(String message){
        super(message);
    }
}
