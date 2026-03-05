package com.cts.eventsphere.dto.contract;

import com.cts.eventsphere.model.data.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request Dto for Contract Dto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

public record ContractRequestDto(
        String vendorId,
        String eventId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal value,
        ContractStatus status
) {
}
