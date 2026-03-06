package com.cts.eventsphere.exception.delivery;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception raised when Delivery is not found.
 * * @author 2480177
 *
 * @version 1.0
 * @since 05-03-2026
 */

@Slf4j
public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(String id) {
        super("Delivery not found with ID: " + id);
        log.error("Delivery not found with ID: {}", id);
    }
}