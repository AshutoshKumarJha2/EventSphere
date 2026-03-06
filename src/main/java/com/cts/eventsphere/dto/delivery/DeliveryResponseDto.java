package com.cts.eventsphere.dto.delivery;

import com.cts.eventsphere.model.data.DeliveryStatus;

import java.time.LocalDateTime;

/**
 * Response Dto for Delivery Dto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

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
