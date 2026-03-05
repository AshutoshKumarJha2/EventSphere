package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;

import java.util.List;

public interface DeliveryService {
    DeliveryResponseDto createDelivery(DeliveryRequestDto request);

    DeliveryResponseDto getDeliveryById(String deliveryId);

    List<DeliveryResponseDto> getAllDeliveries();

    DeliveryResponseDto updateDelivery(String deliveryId, DeliveryRequestDto request);

    void deleteDelivery(String deliveryId);
}
