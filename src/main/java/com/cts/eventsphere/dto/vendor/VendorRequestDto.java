package com.cts.eventsphere.dto.vendor;

import com.cts.eventsphere.model.data.VendorStatus;

/**
 * Request Dto for Budget Dto
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */

public record VendorRequestDto (
        String name,
        String contactInfo,
        VendorStatus status
) {
}
