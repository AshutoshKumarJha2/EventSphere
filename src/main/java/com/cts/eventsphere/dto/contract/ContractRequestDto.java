package com.cts.eventsphere.dto.contract;

import com.cts.eventsphere.model.data.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContractRequestDto(
        String vendorId,
        String eventId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal value,
        ContractStatus status
) {
}
