package com.cts.eventsphere.dto.mapper.delivery;

import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.model.Delivery;
import org.springframework.stereotype.Component;

@Component
public class DeliveryResponseDtoMapper {
    public DeliveryResponseDto toDto(Delivery delivery){
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
