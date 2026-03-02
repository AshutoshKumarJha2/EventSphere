package com.cts.eventsphere.dto.budget;

import java.math.BigDecimal;

/**
 * Request Dto for Budget Dto
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
public record BudgetRequestDto(
        BigDecimal plannedAmount
) {
}
