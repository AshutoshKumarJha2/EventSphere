package com.cts.eventsphere.dto.expense;

import com.cts.eventsphere.model.data.ExpenseStatus;

import java.math.BigDecimal;


/**
 * Response Dto representing expense details
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
public record ExpenseResponseDto(
        String expenseId,
        String eventId,
        String description,
        BigDecimal amount,
        String date,
        String approvedBy,
        ExpenseStatus status,
        String createdAt,
        String updatedAt
) {}





