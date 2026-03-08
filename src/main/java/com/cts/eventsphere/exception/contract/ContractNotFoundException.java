package com.cts.eventsphere.exception.contract;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception raised when Contract is not found.
 *
 * @author 2480177
 * @version 1.0
 * @since 05-03-2026
 */

@Slf4j
public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(String id) {
        super("Contract not found with ID: " + id);
        log.error("Contract not found with ID: {}", id);
    }
}