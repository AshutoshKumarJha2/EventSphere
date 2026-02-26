package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * [Detailed description of the class's responsibility]
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
public interface ExpenseRepository extends JpaRepository<Expense, String> {
}
