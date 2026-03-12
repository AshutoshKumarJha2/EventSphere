package com.cts.eventsphere.dto.mapper.budget;

import com.cts.eventsphere.dto.budget.BudgetResponseDto;

import com.cts.eventsphere.model.Budget;
import org.springframework.stereotype.Component;


/**
 * Mapper for converting Budget entity to BudgetResponseDto
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */

@Component
public class BudgetResponseDtoMapper {
    public BudgetResponseDto toDTO(Budget budget) {
        return BudgetResponseDto.builder()
                .budgetId(budget.getBudgetId())
                .eventId(budget.getEvent().getEventId())
                .plannedAmount(budget.getPlannedAmount())
                .actualAmount(budget.getActualAmount())
                .variance(budget.getVariance())
                .createdAt(budget.getCreatedAt() != null ? budget.getCreatedAt().toString() : null)
                .updatedAt(budget.getUpdatedAt() != null ? budget.getUpdatedAt().toString() : null)
                .build();
    }
}
