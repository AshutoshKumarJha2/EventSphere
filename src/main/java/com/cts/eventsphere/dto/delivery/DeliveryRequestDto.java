package com.cts.eventsphere.dto.delivery;

import com.cts.eventsphere.model.data.DeliveryStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Request DTO for capturing delivery details from the vendor.
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

public record DeliveryRequestDto(
        @NotBlank(message = "Delivery must be linked to an Invoice ID")
        String invoiceId,

        @NotBlank(message = "Item description is required")
        String item,

        @NotNull(message = "Item quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1 unit")
        Integer quantity,

        @NotNull(message = "Delivery schedule date is required")
        LocalDateTime deliveryDate,

        @NotNull(message = "Delivery status is required")
        DeliveryStatus status,

        @NotBlank(message = "Tracking number must be provided")
        String trackingNumber
) {}
