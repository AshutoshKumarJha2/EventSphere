package com.cts.eventsphere.dto.vendor;

import com.cts.eventsphere.model.data.VendorStatus;

/**
 * Request Dto for Vendor Dto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

public record VendorRequestDto (
        String name,
        String contactInfo,
        VendorStatus status
) {
}
