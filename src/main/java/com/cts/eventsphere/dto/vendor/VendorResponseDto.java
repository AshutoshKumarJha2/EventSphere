package com.cts.eventsphere.dto.vendor;

import com.cts.eventsphere.model.data.VendorStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * Response Dto for Vendor Dto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

public record VendorResponseDto(
        @NotBlank(message = "Internal Error: Vendor ID is missing in response")
        String vendorId,

        @NotBlank(message = "Vendor name must be present in response")
        String name,

        @NotBlank(message = "Contact info must be present in response")
        String contactInfo,

        @NotNull(message = "Status must be present in response")
        VendorStatus status,

        @NotNull(message = "Creation timestamp is missing")
        LocalDateTime createdAt,

        @NotNull(message = "Last update timestamp is missing")
        LocalDateTime updatedAt
) {}