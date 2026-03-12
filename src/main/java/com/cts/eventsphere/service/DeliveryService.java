package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.model.data.DeliveryStatus;

import java.util.List;

/**
 * Service interface for Delivery Operations
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

public interface DeliveryService {
    DeliveryResponseDto createDelivery(DeliveryRequestDto request);

    DeliveryResponseDto getDeliveryById(String deliveryId);

    List<DeliveryResponseDto> getAllDeliveries();

    DeliveryResponseDto updateDeliveryStatus(String deliveryId, DeliveryStatus status);

    DeliveryResponseDto updateDelivery(String deliveryId, DeliveryRequestDto request);

    void deleteDelivery(String deliveryId);
}
