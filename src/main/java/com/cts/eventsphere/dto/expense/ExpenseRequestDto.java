package com.cts.eventsphere.dto.expense;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Request Dto for Expense Entity
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
public record ExpenseRequestDto(
        @NotBlank(message = "Description cannot be empty")
        @Size(max = 255 , message = "Description provided is too long")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0",inclusive = false,message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "Date is required")
        LocalDate date
) { }
