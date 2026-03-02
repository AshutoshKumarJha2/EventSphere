package com.cts.eventsphere.dto.mapper.expense;

import com.cts.eventsphere.dto.expense.ExpenseRequestDto;
import com.cts.eventsphere.model.Expense;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting ExpenseRequestDto to Expense Entity
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@Component
public class ExpenseRequestDtoMapper {
    /**
     * converts request dto to Expense Entity
     * @param dto the request dt0
     * @return Expense Entity
     */
    public Expense toEntity(ExpenseRequestDto dto){
        Expense expense = new Expense();
        expense.setDescription(dto.description());
        expense.setAmount(dto.amount());
        expense.setDate(dto.date());
        return expense;
    }
}
