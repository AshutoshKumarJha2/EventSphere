package com.cts.eventsphere.dto.vendor;

import com.cts.eventsphere.model.data.VendorStatus;
import jakarta.validation.constraints.*;

/**
 * Request Dto for Vendor Dto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

public record VendorRequestDto(
        @NotBlank(message = "Vendor name cannot be empty")
        @Size(max = 100, message = "Vendor name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Contact information is required for communication")
        String contactInfo,

        @NotNull(message = "Vendor status (ACTIVE/INACTIVE) must be specified")
        VendorStatus status
) {}
