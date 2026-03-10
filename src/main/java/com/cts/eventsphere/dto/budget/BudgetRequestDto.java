package com.cts.eventsphere.dto.budget;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Request Dto for Budget Dto
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@Builder
public record BudgetRequestDto(
        @NotNull(message = "Planned Amount is required")
        @DecimalMin(value = "0.0",inclusive = false,message = "Planned Amount must be greater than 0")
        BigDecimal plannedAmount
) {
}
