package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.budget.BudgetRequestDto;
import com.cts.eventsphere.dto.budget.BudgetResponseDto;
import com.cts.eventsphere.dto.mapper.budget.BudgetRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.budget.BudgetResponseDtoMapper;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.model.Budget;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.repository.BudgetRepository;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation for Budget Service
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetServiceImpl  implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final EventRepository eventRepository;
    private final BudgetRequestDtoMapper budgetRequestDtoMapper;
    private final BudgetResponseDtoMapper budgetResponseDtoMapper;

    /**
     * Creates a Budget for a given Event
     */
    @Override
    public BudgetResponseDto createBudget(String eventId, BudgetRequestDto request) throws EventNotFoundException{
        log.info("Starting budget creation for eventId: {} with details: {}", eventId, request);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        Budget budget = budgetRequestDtoMapper.toEntity(request , event);
        Budget savedBudget = budgetRepository.save(budget);
        BudgetResponseDto response = budgetResponseDtoMapper.toDTO(savedBudget);
        log.info("Successfully saved budget. Generated Budget ID: {}", savedBudget.getBudgetId());
        return response;
    }
}
