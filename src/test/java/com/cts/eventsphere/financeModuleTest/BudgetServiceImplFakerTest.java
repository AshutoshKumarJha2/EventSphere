package com.cts.eventsphere.financeModuleTest;

import com.cts.eventsphere.dto.budget.BudgetRequestDto;
import com.cts.eventsphere.dto.budget.BudgetResponseDto;
import com.cts.eventsphere.dto.mapper.budget.BudgetRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.budget.BudgetResponseDtoMapper;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.model.Budget;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.repository.BudgetRepository;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.service.impl.BudgetServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplFakerTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private BudgetRequestDtoMapper budgetRequestDtoMapper;

    @Mock
    private BudgetResponseDtoMapper budgetResponseDtoMapper;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void testCreateBudgetSuccess() {
        String eventId = faker.internet().uuid();
        Event event = new Event();
        event.setEventId(eventId);

        BudgetRequestDto request = new BudgetRequestDto(
                BigDecimal.valueOf(faker.number().randomDouble(2, 1000, 5000))
        );

        Budget budget = new Budget();
        budget.setBudgetId(faker.internet().uuid());
        budget.setEvent(event);
        budget.setPlannedAmount(request.plannedAmount());

        BudgetResponseDto response = new BudgetResponseDto(
                budget.getBudgetId(),
                eventId,
                budget.getPlannedAmount(),
                budget.getActualAmount(),
                budget.getVariance(),
                faker.date().birthday().toString(),
                faker.date().birthday().toString()
        );

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Mockito.when(budgetRequestDtoMapper.toEntity(eq(request), eq(event))).thenReturn(budget);
        Mockito.when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
        Mockito.when(budgetResponseDtoMapper.toDTO(budget)).thenReturn(response);

        BudgetResponseDto result = budgetService.createBudget(eventId, request);

        assertNotNull(result);
        assertEquals(request.plannedAmount(), result.plannedAmount());
        assertEquals(eventId, result.eventId());
    }

    @Test
    void testCreateBudgetEventNotFound() {
        String eventId = faker.internet().uuid();
        BudgetRequestDto request = new BudgetRequestDto(
                BigDecimal.valueOf(faker.number().randomDouble(2, 1000, 5000))
        );

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> budgetService.createBudget(eventId, request));
    }
}
