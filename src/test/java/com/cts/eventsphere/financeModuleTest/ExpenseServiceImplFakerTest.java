package com.cts.eventsphere.financeModuleTest;

import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
import com.cts.eventsphere.dto.mapper.expense.ExpenseRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.expense.ExpenseResponseDtoMapper;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Expense;
import com.cts.eventsphere.model.data.ExpenseStatus;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.repository.ExpenseRepository;
import com.cts.eventsphere.service.impl.ExpenseServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * [Detailed description of the class's responsibility]
 *
 * @author 2480081
 * @version 1.0
 * @since 06-03-2026
 */
@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplFakerTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ExpenseRequestDtoMapper expenseRequestDtoMapper;
    @Mock
    private ExpenseResponseDtoMapper expenseResponseDtoMapper;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void testCreateExpense_success() {
        String eventId = faker.idNumber().valid();
        Event event = new Event();
        event.setEventId(eventId);

        ExpenseRequestDto requestDto = new ExpenseRequestDto(
                faker.commerce().productName(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)),
                LocalDate.now()
        );

        Expense expense = new Expense();
        expense.setEvent(event);

        ExpenseResponseDto responseDto = new ExpenseResponseDto(
                faker.idNumber().valid(),
                eventId,
                requestDto.description(),
                requestDto.amount(),
                requestDto.date().toString(),
                null,
                ExpenseStatus.submitted,
                LocalDate.now().toString(),
                LocalDate.now().toString()
        );

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(expenseRequestDtoMapper.toEntity(requestDto)).thenReturn(expense);
        when(expenseRepository.save(expense)).thenReturn(expense);
        when(expenseResponseDtoMapper.toDTO(expense)).thenReturn(responseDto);

        ExpenseResponseDto result = expenseService.createExpense(eventId, requestDto);

        assertEquals(responseDto, result);
        verify(expenseRepository).save(expense);
    }

    @Test
    void testGetAllExpenses() {
        Expense expense = new Expense();
        expense.setDescription(faker.commerce().productName());

        ExpenseResponseDto responseDto = new ExpenseResponseDto(
                faker.idNumber().valid(),
                faker.idNumber().valid(),
                expense.getDescription(),
                BigDecimal.valueOf(200),
                LocalDate.now().toString(),
                null,
                ExpenseStatus.submitted,
                LocalDate.now().toString(),
                LocalDate.now().toString()
        );

        when(expenseRepository.findAll()).thenReturn(List.of(expense));
        when(expenseResponseDtoMapper.toDTO(expense)).thenReturn(responseDto);

        List<ExpenseResponseDto> result = expenseService.getAllExpenses();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void testDeleteExpense_success() {
        String expenseId = faker.idNumber().valid();
        when(expenseRepository.existsById(expenseId)).thenReturn(true);

        expenseService.deleteExpense(expenseId);

        verify(expenseRepository).deleteById(expenseId);
    }

    @Test
    void testDeleteExpense_notFound() {
        String expenseId = faker.idNumber().valid();
        when(expenseRepository.existsById(expenseId)).thenReturn(false);

        assertThrows(ExpenseNotFoundException.class,
                () -> expenseService.deleteExpense(expenseId));
    }

    @Test
    void testUpdateExpenseStatus_success() {
        String expenseId = faker.idNumber().valid();
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);

        ExpenseResponseDto responseDto = new ExpenseResponseDto(
                expenseId,
                faker.idNumber().valid(),
                faker.commerce().productName(),
                BigDecimal.valueOf(500),
                LocalDate.now().toString(),
                null,
                ExpenseStatus.approved,
                LocalDate.now().toString(),
                LocalDate.now().toString()
        );

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));
        when(expenseRepository.save(expense)).thenReturn(expense);
        when(expenseResponseDtoMapper.toDTO(expense)).thenReturn(responseDto);

        ExpenseResponseDto result = expenseService.updateExpenseStatus(expenseId, ExpenseStatus.approved);

        assertEquals(responseDto, result);
        verify(expenseRepository).save(expense);
    }
}
