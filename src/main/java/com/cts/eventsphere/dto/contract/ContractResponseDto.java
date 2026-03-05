package com.cts.eventsphere.dto.contract;

import ch.qos.logback.core.util.Loader;
import com.cts.eventsphere.model.data.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContractResponseDto(
        String contractId,
        String vendorId,
        String eventId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal value,
        ContractStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
