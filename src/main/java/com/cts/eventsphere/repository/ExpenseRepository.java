package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Expense Entity
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
public interface ExpenseRepository extends JpaRepository<Expense, String> {
}
