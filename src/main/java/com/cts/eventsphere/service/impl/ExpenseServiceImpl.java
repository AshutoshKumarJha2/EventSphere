package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
import com.cts.eventsphere.dto.mapper.expense.ExpenseRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.expense.ExpenseResponseDtoMapper;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Expense;
import com.cts.eventsphere.model.data.ExpenseStatus;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.repository.ExpenseRepository;
import com.cts.eventsphere.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [Detailed description of the class's responsibility]
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EventRepository eventRepository;
    private final ExpenseRequestDtoMapper expenseRequestDtoMapper;
    private final ExpenseResponseDtoMapper expenseResponseDtoMapper;

    /**
     *
     * @param eventId of the event
     * @param request of the expense
     * @return saved expense along with the HTTP status code
     */
    @Override
    public ExpenseResponseDto createExpense(String eventId , ExpenseRequestDto request) throws EventNotFoundException{
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        Expense expense = expenseRequestDtoMapper.toEntity(request);
        expense.setEvent(event);
        return expenseResponseDtoMapper.toDTO(expenseRepository.save(expense));

    }

    /**
     * Retrieve all expenses from the system
     */
    @Override
    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(expenseResponseDtoMapper::toDTO)
                .toList();
    }

    /**
     * get Expense of an event
     */
    @Override
    public List<ExpenseResponseDto> getExpenseByEvent(String eventId) throws EventNotFoundException{
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        return expenseRepository.findAll().stream()
                .filter(expense -> expense.getEvent().getEventId().equals(event.getEventId()))
                .map(expenseResponseDtoMapper::toDTO)
                .toList();
    }

    /**
     * Delete an Expense
     */
    @Override
    public void deleteExpense(String expenseId) throws ExpenseNotFoundException{
        if(!expenseRepository.existsById(expenseId)){
            throw new ExpenseNotFoundException(expenseId);
        }
        expenseRepository.deleteById(expenseId);
    }

    /**
     * Updates the status of an Expense
     */
    @Override
    public ExpenseResponseDto updateExpenseStatus(String expenseId , ExpenseStatus status) throws ExpenseNotFoundException{
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));
        expense.setStatus(status);
        Expense updatedExpense = expenseRepository.save(expense);
        return expenseResponseDtoMapper.toDTO(updatedExpense);

    }


}
