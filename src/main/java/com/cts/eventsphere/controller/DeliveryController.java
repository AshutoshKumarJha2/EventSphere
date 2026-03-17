package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.exception.delivery.DeliveryNotFoundException;
import com.cts.eventsphere.model.data.DeliveryStatus;
import com.cts.eventsphere.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Delivery entity management.
 * Provides endpoints for tracking equipment and resource deliveries for events.
 * Adheres to mandatory coding standards for maintainability and readability.
 *
 * @author 2480177
 * @version 1.1
 * @since 03-03-2026
 */

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * Creates a new delivery record in the system.
     * This endpoint is restricted to users with the 'VENDOR' role.
     *
     * @param request the delivery details to be created, validated by @Valid
     * @return the created delivery response DTO with 201 Created status
     */
    @PostMapping
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<DeliveryResponseDto> create(@Valid @RequestBody DeliveryRequestDto request) {
        log.info("REST request to create delivery for invoice: {}", request.invoiceId());
        return new ResponseEntity<>(deliveryService.createDelivery(request), HttpStatus.CREATED);
    }

    /**
     * Retrieves a delivery record by its unique ID.
     * Returns a 200 OK status with the delivery details if found.
     *
     * @param id the unique identifier of the delivery
     * @return the delivery details wrapped in a ResponseEntity
     * @throws DeliveryNotFoundException if the provided delivery ID does not exist in the system
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDto> getById(@PathVariable String id) {
        log.info("REST request to fetch delivery with ID: {}", id);
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }

    /**
     * Fetches a list of all deliveries currently registered in the system.
     * Useful for administrative overview or tracking multiple shipments.
     *
     * @return a list of all delivery response DTOs wrapped in a ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<DeliveryResponseDto>> getAll() {
        log.info("REST request to fetch all deliveries");
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    /**
     * Updates the status of an existing delivery.
     * Restricted to users with the 'VENDOR' role to track shipping progress.
     *
     * @param id the unique identifier of the delivery to be updated
     * @param status the new status (e.g., SHIPPED, DELIVERED) to apply
     * @return the updated delivery details wrapped in a ResponseEntity
     * @throws DeliveryNotFoundException if the specified delivery ID is not found
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<DeliveryResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam DeliveryStatus status) {

        log.info("REST request to update status for delivery ID: {} to {}", id, status);
        return ResponseEntity.ok(deliveryService.updateDeliveryStatus(id, status));
    }

    /**
     * Performs a full update of an existing delivery record.
     * Overwrites all delivery fields with the information provided in the request body.
     * Restricted to users with the 'VENDOR' role.
     *
     * @param id the unique identifier of the delivery to update
     * @param request the new details (DTO) to be saved
     * @return the updated delivery response DTO wrapped in a ResponseEntity
     * @throws DeliveryNotFoundException if the specified delivery ID is not found
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<DeliveryResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody DeliveryRequestDto request) {

        log.info("REST request to update full details for delivery ID: {}", id);
        return ResponseEntity.ok(deliveryService.updateDelivery(id, request));
    }

    /**
     * Permanently deletes a delivery record from the database.
     * This operation is restricted to 'VENDOR' and 'ADMIN' roles.
     *
     * @param id the unique identifier of the delivery to remove
     * @return an empty ResponseEntity with 204 No Content status on success
     * @throws DeliveryNotFoundException if the delivery ID is not found in the database
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("REST request to delete delivery with ID: {}", id);
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
}

