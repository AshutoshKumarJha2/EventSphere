package com.cts.eventsphere.exception.vendor;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception raised when Vendor is not found.
 *
 * @author 2480177
 * @version 1.0
 * @since 05-03-2026
 */

@Slf4j
public class VendorNotFoundException extends RuntimeException {
    public VendorNotFoundException(String id) {
        super("Vendor not found with ID: " + id);
        log.error("Vendor not found with ID: {}", id);
    }
}
