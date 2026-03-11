package com.cts.eventsphere.dto.budget;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Budget dto representing budget details
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
@Builder
public record BudgetResponseDto(
        String budgetId,
        String eventId,
        BigDecimal plannedAmount,
        BigDecimal actualAmount,
        BigDecimal variance,
        String createdAt,
        String updatedAt
) {
}
