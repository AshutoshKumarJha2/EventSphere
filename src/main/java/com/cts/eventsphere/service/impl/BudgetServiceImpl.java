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
import com.cts.eventsphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation for Service Interface for Budget Class.
 * Provides business logic for managing event budgets, including the allocation
 * and tracking of total funds. This service triggers notifications to event
 * organizers whenever a budget is established or modified to ensure financial
 * transparency within the system.
 *
 * @author 2480081
 * @version 1.2
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
    private final NotificationService notificationService;

    /**
     * Creates a new budget for a specific event and triggers a notification to the organizer.
     *
     * @param eventId the unique identifier of the event for which the budget is being created
     * @param request the DTO containing budget details, such as total allocated amount
     * @return the response DTO representing the newly created budget
     * @throws EventNotFoundException if no event exists with the given ID
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
        notificationService.sendNotification(
                eventId,
                event.getOrganizerId(),
                "Budget Created for Event: " + event.getName() +
                        " | Total Amount: " + request.plannedAmount(),
                "BUDGET_CREATED"
        );
        return response;
    }
}
