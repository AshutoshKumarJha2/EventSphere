package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.budget.BudgetRequestDto;
import com.cts.eventsphere.dto.budget.BudgetResponseDto;
import com.cts.eventsphere.exception.event.EventNotFoundException;

/**
 * Service interface for Budget Operations
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
public interface BudgetService {
    BudgetResponseDto createBudget(String eventId , BudgetRequestDto request) throws EventNotFoundException;


}
