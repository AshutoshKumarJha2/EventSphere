package com.cts.eventsphere.dto.mapper.expense;

import com.cts.eventsphere.dto.expense.ExpenseResponseDto;
import com.cts.eventsphere.model.Expense;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Expense Entity to ExpenseResponseDto
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
@Component
public class ExpenseResponseDtoMapper {
    public ExpenseResponseDto toDTO(Expense expense) {
        return new ExpenseResponseDto(
                expense.getExpenseId(),
                expense.getEvent().getEventId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDate().toString(), // Or use a DateTimeFormatter
                expense.getApprovedBy(),
                expense.getStatus(),
                expense.getCreatedAt().toString(),
                expense.getUpdatedAt().toString()
        );
    }
}
