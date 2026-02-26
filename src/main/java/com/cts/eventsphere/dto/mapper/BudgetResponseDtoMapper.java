package com.cts.eventsphere.dto.mapper;

import com.cts.eventsphere.dto.BudgetResponseDto;
import com.cts.eventsphere.dto.ExpenseResponseDto;
import com.cts.eventsphere.model.Budget;
import com.cts.eventsphere.model.Expense;

/**
 * [Detailed description of the class's responsibility]
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
public class BudgetResponseDtoMapper {
    public BudgetResponseDto toDTO(Budget budget) {
        return new BudgetResponseDto(
                budget.getBudgetId(),
                budget.getEventId(),
                budget.getPlannedAmount(),
                budget.getActualAmount(),
                budget.getVariance(),
                budget.getCreatedAt().toString(),
                budget.getUpdatedAt().toString()
        );
    }
}
