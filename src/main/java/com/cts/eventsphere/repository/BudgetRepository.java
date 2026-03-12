package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for the Budget Entity
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
public interface BudgetRepository extends JpaRepository<Budget , String> {
}
