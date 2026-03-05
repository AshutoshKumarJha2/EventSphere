package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public DeliveryResponseDto create(@RequestBody DeliveryRequestDto request) {
        log.info("Creating delivery");
        return deliveryService.createDelivery(request);
    }

    @GetMapping("/{id}")
    public DeliveryResponseDto getById(@PathVariable String id) {
        log.info("Fetching delivery with id: {}", id);
        return deliveryService.getDeliveryById(id);
    }

    @GetMapping
    public List<DeliveryResponseDto> getAll() {
        log.info("Fetching all deliveries");
        return deliveryService.getAllDeliveries();
    }

    @PutMapping("/{id}")
    public DeliveryResponseDto update(
            @PathVariable String id,
            @RequestBody DeliveryRequestDto request) {

        log.info("Updating delivery with id: {}", id);
        return deliveryService.updateDelivery(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.info("Deleting delivery with id: {}", id);
        deliveryService.deleteDelivery(id);
    }
}

