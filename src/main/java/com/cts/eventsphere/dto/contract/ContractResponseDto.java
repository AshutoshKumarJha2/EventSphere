package com.cts.eventsphere.dto.contract;

import com.cts.eventsphere.model.data.ContractStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response Dto for Contract Dto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

public record ContractResponseDto(
        @NotBlank(message = "Response Error: Contract ID is missing")
        String contractId,

        @NotBlank(message = "Response Error: Vendor ID is missing")
        String vendorId,

        @NotBlank(message = "Response Error: Event ID is missing")
        String eventId,

        @NotNull(message = "Response Error: Start date is missing")
        LocalDateTime startDate,

        @NotNull(message = "Response Error: End date is missing")
        LocalDateTime endDate,

        @NotNull(message = "Response Error: Value is missing")
        @Positive(message = "Response Error: Value must be positive")
        BigDecimal value,

        @NotNull(message = "Response Error: Status is missing")
        ContractStatus status,

        @NotNull(message = "Response Error: Created timestamp is missing")
        LocalDateTime createdAt,

        @NotNull(message = "Response Error: Updated timestamp is missing")
        LocalDateTime updatedAt
) {}