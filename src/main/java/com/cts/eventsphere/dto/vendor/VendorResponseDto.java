package com.cts.eventsphere.dto.vendor;

import com.cts.eventsphere.model.data.VendorStatus;

import java.time.LocalDateTime;

public record VendorResponseDto(
        String vendorId,
        String name,
        String contactInfo,
        VendorStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
