package com.cts.eventsphere.exception.invoice;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception raised when the PDF generation process for an invoice encounters a technical failure.
 *
 * @author 2480177
 * @version 1.0
 * @since 14-03-2026
 */
@Slf4j
public class InvoicePdfGenerationException extends RuntimeException {
    public InvoicePdfGenerationException(String message) {
        super(message);
        log.error(message);
    }
}