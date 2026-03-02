package com.cts.eventsphere.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Request Dto for Expense Entity
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
public record ExpenseRequestDto(
        String description,
        BigDecimal amount,
        LocalDateTime date
) { }
