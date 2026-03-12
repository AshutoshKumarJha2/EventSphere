package com.cts.eventsphere.financeModuleTest;

import com.cts.eventsphere.controller.BudgetController;
import com.cts.eventsphere.dto.budget.BudgetRequestDto;
import com.cts.eventsphere.dto.budget.BudgetResponseDto;
import com.cts.eventsphere.service.BudgetService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class BudgetControllerFakerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void testSetBudget() {
        String eventId = faker.internet().uuid();
        BudgetRequestDto request = new BudgetRequestDto(
                BigDecimal.valueOf(faker.number().randomDouble(2, 1000, 5000))
        );

        BudgetResponseDto response = new BudgetResponseDto(
                faker.internet().uuid(),
                eventId,
                request.plannedAmount(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                faker.date().birthday().toString(),
                faker.date().birthday().toString()
        );

        Mockito.when(budgetService.createBudget(eq(eventId), any(BudgetRequestDto.class)))
                .thenReturn(response);

        ResponseEntity<BudgetResponseDto> result = budgetController.setBudget(eventId, request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(request.plannedAmount(), result.getBody().plannedAmount());
        assertEquals(eventId, result.getBody().eventId());
    }
}
