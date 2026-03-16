package com.cts.eventsphere.exception.invoice;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception raised when an Invoice cannot be located in the persistence layer.
 *
 * @author 2480177
 * @version 1.0
 * @since 05-03-2026
 */

@Slf4j
public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(String id) {
        super("Invoice not found with ID: " + id);
        log.error("Invoice not found with ID: {}", id);
    }
}