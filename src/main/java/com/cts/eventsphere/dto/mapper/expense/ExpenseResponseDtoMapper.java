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
//        return new ExpenseResponseDto(
//                expense.getExpenseId(),
//                expense.getEvent().getEventId(),
//                expense.getDescription(),
//                expense.getAmount(),
//                expense.getDate().toString(), // Or use a DateTimeFormatter
//                expense.getApprovedBy(),
//                expense.getStatus(),
//                expense.getCreatedAt().toString(),
//                expense.getUpdatedAt().toString()
//        );
        return ExpenseResponseDto.builder()
                .expenseId(expense.getExpenseId())
                .eventId(expense.getEvent().getEventId())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .date(expense.getDate() != null ? expense.getDate().toString() : null)
                .approvedBy(expense.getApprovedBy())
                .status(expense.getStatus())
                .createdAt(expense.getCreatedAt() != null ? expense.getCreatedAt().toString() : null)
                .updatedAt(expense.getUpdatedAt() != null ? expense.getUpdatedAt().toString() : null)
                .build();
    }
}
