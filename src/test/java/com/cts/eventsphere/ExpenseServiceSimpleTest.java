package com.cts.eventsphere;


import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Expense;
import com.cts.eventsphere.model.data.ExpenseStatus;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.repository.ExpenseRepository;
import com.cts.eventsphere.dto.mapper.expense.ExpenseRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.expense.ExpenseResponseDtoMapper;
import com.cts.eventsphere.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceSimpleTest {

    @Mock private ExpenseRepository expenseRepo;
    @Mock private EventRepository eventRepo;
    @Mock private ExpenseRequestDtoMapper requestMapper;
    @Mock private ExpenseResponseDtoMapper responseMapper;

    @InjectMocks private ExpenseServiceImpl expenseService;

    private Expense testExpense;
    private Event testEvent;

    @BeforeEach
    void setup() {
        testEvent = new Event();
        testEvent.setEventId("EVT-001");

        testExpense = new Expense();
        testExpense.setExpenseId("EXP-100");
        testExpense.setStatus(ExpenseStatus.submitted);
        testExpense.setEvent(testEvent);
    }

    @Test
    void testCreateFunctionality() {
        // Arrange: Prepare inputs
        ExpenseRequestDto dto = new ExpenseRequestDto("Dinner", new BigDecimal("50"), null);

        // Mocking: Tell the fake database what to do
        when(eventRepo.findById("EVT-001")).thenReturn(Optional.of(testEvent));
        when(requestMapper.toEntity(dto)).thenReturn(testExpense);
        when(expenseRepo.save(any())).thenReturn(testExpense);

        // Act: Run the create method
        expenseService.createExpense("EVT-001", dto);

        // Assert: Verify it actually saved to the "database"
        verify(expenseRepo, times(1)).save(testExpense);
        System.out.println("Success: Expense created and linked to Event EVT-001");
    }

    @Test
    void testUpdateStatusFunctionality() {
        // Arrange: Expense starts as 'submitted'
        when(expenseRepo.findById("EXP-100")).thenReturn(Optional.of(testExpense));
        when(expenseRepo.save(any())).thenReturn(testExpense);

        // Act: Change status to 'approved'
        expenseService.updateExpenseStatus("EXP-100", ExpenseStatus.approved);

        // Assert: Check if the status inside the object actually changed
        assertEquals(ExpenseStatus.approved, testExpense.getStatus());
        System.out.println("Success: Status changed from submitted to " + testExpense.getStatus());
    }

    @Test
    void testDeleteFunctionality() {
        // Arrange: Pretend the expense exists in DB
        when(expenseRepo.existsById("EXP-100")).thenReturn(true);

        // Act: Call delete
        expenseService.deleteExpense("EXP-100");

        // Assert: Verify the repository's delete method was triggered
        verify(expenseRepo).deleteById("EXP-100");
        System.out.println("Success: Expense EXP-100 removed from system");
    }

    /**
     * GET ALL EXPENSES TEST
     * Verifies that the service retrieves everything in the database.
     */
    @Test
    void testGetAllExpenses() {
        // Arrange: Create a list with one expense
        when(expenseRepo.findAll()).thenReturn(List.of(testExpense));
        when(responseMapper.toDTO(testExpense)).thenReturn(
                new ExpenseResponseDto("EXP-100", "EVT-001", "Dinner", new BigDecimal("50"), "2026-03-01", null, ExpenseStatus.submitted, "now", "now")
        );

        // Act
        List<ExpenseResponseDto> result = expenseService.getAllExpenses();

        // Assert
        assertEquals(1, result.size());
        assertEquals("EXP-100", result.get(0).expenseId());
        System.out.println("Verified: Successfully retrieved total list of " + result.size() + " expense(s).");
    }

    /**
     * GET EXPENSES BY EVENT TEST
     * Verifies that the service filters expenses for a specific event correctly.
     */
    @Test
    void testGetExpenseByEvent() {
        // Arrange: Mock the event check and the data return
        when(eventRepo.findById("EVT-001")).thenReturn(Optional.of(testEvent));
        when(expenseRepo.findAll()).thenReturn(List.of(testExpense));
        when(responseMapper.toDTO(testExpense)).thenReturn(
                new ExpenseResponseDto("EXP-100", "EVT-001", "Dinner", new BigDecimal("50"), "2026-03-01", null, ExpenseStatus.submitted, "now", "now")
        );

        // Act
        List<ExpenseResponseDto> result = expenseService.getExpenseByEvent("EVT-001");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals("EVT-001", result.get(0).eventId());
        System.out.println("Verified: Filtered expenses. Only showing items for Event: " + result.get(0).eventId());
    }
}