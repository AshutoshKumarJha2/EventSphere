package com.cts.eventsphere.dto.mapper.delivery;

import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.model.Delivery;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Delivery entity to DeliveryResponseDto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

@Component
public class DeliveryResponseDtoMapper {
    public DeliveryResponseDto toDto(Delivery delivery){
        if (delivery == null) {
            return null;
        }
        return new DeliveryResponseDto(
                delivery.getDeliveryId(),
                delivery.getInvoiceId(),
                delivery.getItem(),
                delivery.getQuantity(),
                delivery.getDeliveryDate(),
                delivery.getStatus(),
                delivery.getTrackingNumber(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt()
        );
    }
}
