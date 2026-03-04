package com.cts.eventsphere.dto.delivery;

import com.cts.eventsphere.model.data.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryResponseDto(
        String deliveryId,
        String invoiceId,
        String item,
        Integer quantity,
        LocalDateTime deliveryDate,
        DeliveryStatus status,
        String trackingNumber,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
