package com.cts.eventsphere.dto.mapper.budget;

import com.cts.eventsphere.dto.budget.BudgetRequestDto;
import com.cts.eventsphere.model.Budget;
import com.cts.eventsphere.model.Event;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert BudgetRequestDto to Budget Entity
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@Component
public class BudgetRequestDtoMapper {
    public Budget toEntity(BudgetRequestDto dto, Event event){
        Budget budget = new Budget();
        budget.setEvent(event);
        budget.setPlannedAmount(dto.plannedAmount());
//        budget.setActualAmount(dto.actualAmount);
//        budget.setVariance(dto.Variance);
        return budget;
    }
}
