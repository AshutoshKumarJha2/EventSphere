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
import com.cts.eventsphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link ExpenseService} interface.
 * Provides business logic for managing financial records associated with events.
 * This includes creating, retrieving, updating status, and deleting expenses.
 * It also integrates with {@link NotificationService} to alert event organizers
 * of any financial changes.
 *
 * @author 2480081
 * @version 1.1
 * @since 01-03-2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EventRepository eventRepository;
    private final ExpenseRequestDtoMapper expenseRequestDtoMapper;
    private final ExpenseResponseDtoMapper expenseResponseDtoMapper;
    private final NotificationService notificationService;

    /**
     * Records a new expense for a specific event and notifies the organizer.
     *
     * @param eventId the unique identifier of the event to link the expense to
     * @param request the DTO containing expense details like amount and category
     * @return the response DTO representing the saved expense record
     * @throws EventNotFoundException if no event exists with the provided ID
     */
    @Override
    public ExpenseResponseDto createExpense(String eventId , ExpenseRequestDto request) throws EventNotFoundException{
        log.info("Creating expense for eventId: {} with details: {}", eventId, request);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        Expense expense = expenseRequestDtoMapper.toEntity(request);
        expense.setEvent(event);
        Expense savedExpense = expenseRepository.save(expense);
        notificationService.sendNotification(
                eventId,
                event.getOrganizerId(),
                "New Expense Created: " + request.description() +
                        " | Amount: " + request.amount() +
                        " | Date: " + request.date(),
                "FINANCE"
        );
        ExpenseResponseDto response = expenseResponseDtoMapper.toDTO(savedExpense);
        log.info("Successfully saved expense for eventId: {}. Response: {}", eventId, response);
        return response;

    }

    /**
     * Retrieves all expenses available in the system across all events.
     *
     * @return a list of response DTOs representing all recorded expenses
     */
    @Override
    public List<ExpenseResponseDto> getAllExpenses() {
        log.info("Fetching all expenses from the database");
        List<ExpenseResponseDto> expenses = expenseRepository.findAll().stream()
                .map(expenseResponseDtoMapper::toDTO)
                .toList();
        log.info("Retrieved {} expenses in total", expenses.size());
        return expenses;
    }

    /**
     * Retrieves all expenses associated with a specific event.
     *
     * @param eventId the unique identifier of the event
     * @return a list of response DTOs for the specified event's expenses
     * @throws EventNotFoundException if the parent event does not exist
     */
    @Override
    public List<ExpenseResponseDto> getExpenseByEvent(String eventId) throws EventNotFoundException{
        log.info("Fetching expenses for eventId: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        List<ExpenseResponseDto> expenses = expenseRepository.findAll().stream()
                .filter(expense -> expense.getEvent().getEventId().equals(event.getEventId()))
                .map(expenseResponseDtoMapper::toDTO)
                .toList();
        log.info("Retrieved {} expenses for eventId: {}", expenses.size(), eventId);
        return expenses;
    }

    /**
     * Deletes an expense record and triggers a system-level notification.
     *
     * @param expenseId the unique identifier of the expense to delete
     * @throws ExpenseNotFoundException if no expense exists with the given ID
     */
    @Override
    public void deleteExpense(String expenseId) throws ExpenseNotFoundException{
        log.info("Attempting to delete expense with ID: {}", expenseId);
        if(!expenseRepository.existsById(expenseId)){
            throw new ExpenseNotFoundException(expenseId);
        }
        expenseRepository.deleteById(expenseId);
        log.info("Successfully deleted expense with ID: {}", expenseId);
        notificationService.sendNotification(
                expenseId,
                "finance-system@eventsphere.com",
                "Expense Record Deleted with ID: " + expenseId,
                "FINANCE_DELETED"
        );
    }

    /**
     * Updates the status of an existing expense and notifies the event organizer.
     *
     * @param expenseId the unique identifier of the expense to update
     * @param status the new status to apply (e.g., APPROVED, REJECTED)
     * @return the response DTO representing the updated expense
     * @throws ExpenseNotFoundException if no expense exists with the given ID
     */
    @Override
    public ExpenseResponseDto updateExpenseStatus(String expenseId , ExpenseStatus status) throws ExpenseNotFoundException{
        log.info("Request to update status for expenseId: {} to {}", expenseId, status);
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));
        expense.setStatus(status);
        Expense updatedExpense = expenseRepository.save(expense);
        ExpenseResponseDto response = expenseResponseDtoMapper.toDTO(updatedExpense);
        log.info("Successfully updated expenseId: {} to status: {}", expenseId, response.status());
        notificationService.sendNotification(
                expense.getEvent().getEventId(),
                expense.getEvent().getOrganizerId(),
                "Expense Status Update: " + expense.getDescription() + " is now " + status,
                "FINANCE_UPDATE"
        );
        return response;

    }


}
