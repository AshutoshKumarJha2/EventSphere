package com.cts.eventsphere.dto.mapper.delivery;

import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.model.Delivery;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Delivery entity to DeliveryRequestDto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

@Component
public class DeliveryRequestDtoMapper {
    public Delivery toEntity(DeliveryRequestDto dto){
        Delivery delivery = new Delivery();
        delivery.setInvoiceId(dto.invoiceId());
        delivery.setItem(dto.item());
        delivery.setQuantity(dto.quantity());
        delivery.setDeliveryDate(dto.deliveryDate());
        delivery.setStatus(dto.status());
        delivery.setTrackingNumber(dto.trackingNumber());
        return delivery;
    }
}
