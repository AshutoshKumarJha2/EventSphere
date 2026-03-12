package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.model.data.DeliveryStatus;
import com.cts.eventsphere.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Delivery Entity
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public @Valid DeliveryResponseDto create(@Valid @RequestBody DeliveryRequestDto request) {
        log.info("Creating delivery");
        return deliveryService.createDelivery(request);
    }

    @GetMapping("/{id}")
    public @Valid DeliveryResponseDto getById(@PathVariable String id) {
        log.info("Fetching delivery with id: {}", id);
        return deliveryService.getDeliveryById(id);
    }

    @GetMapping
    public List<@Valid DeliveryResponseDto> getAll() {
        log.info("Fetching all deliveries");
        return deliveryService.getAllDeliveries();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('VENDOR')")
    public @Valid DeliveryResponseDto updateStatus(
            @PathVariable String id,
            @RequestParam DeliveryStatus status) {

        log.info("Request to update delivery status for ID={} to {}", id, status);
        return deliveryService.updateDeliveryStatus(id, status);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public @Valid DeliveryResponseDto update(
            @PathVariable String id,
            @Valid @RequestBody DeliveryRequestDto request) {

        log.info("Updating delivery with id: {}", id);
        return deliveryService.updateDelivery(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public void delete(@PathVariable String id) {
        log.info("Deleting delivery with id: {}", id);
        deliveryService.deleteDelivery(id);
    }
}

