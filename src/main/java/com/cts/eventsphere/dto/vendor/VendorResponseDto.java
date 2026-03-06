package com.cts.eventsphere.dto.vendor;

import com.cts.eventsphere.model.data.VendorStatus;

import java.time.LocalDateTime;

/**
 * Response Dto for Vendor Dto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

public record VendorResponseDto(
        String vendorId,
        String name,
        String contactInfo,
        VendorStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
