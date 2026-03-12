package com.cts.eventsphere.dto.contract;

import com.cts.eventsphere.model.data.ContractStatus;
import jakarta.validation.constraints.*;

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
        @NotBlank(message = "Vendor ID is required to link the contract")
        String vendorId,

        @NotBlank(message = "Event ID is required to link the contract")
        String eventId,

        @NotNull(message = "Start date is required")
        @FutureOrPresent(message = "Contract cannot start in the past")
        LocalDateTime startDate,

        @NotNull(message = "End date is required")
        @Future(message = "Contract end date must be in the future")
        LocalDateTime endDate,

        @NotNull(message = "Contract value is required")
        @Positive(message = "Contract value must be a positive amount")
        BigDecimal value,

        @NotNull(message = "Contract status is required")
        ContractStatus status
) {}
