package com.cts.eventsphere.dto.mapper.budget;

import com.cts.eventsphere.dto.budget.BudgetResponseDto;

import com.cts.eventsphere.model.Budget;


/**
 * Mapper for converting Budget entity to BudgetResponseDto
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
public class BudgetResponseDtoMapper {
    public BudgetResponseDto toDTO(Budget budget) {
        return new BudgetResponseDto(
                budget.getBudgetId(),
                budget.getEvent().getEventId(),
                budget.getPlannedAmount(),
                budget.getActualAmount(),
                budget.getVariance(),
                budget.getCreatedAt().toString(),
                budget.getUpdatedAt().toString()
        );
    }
}
