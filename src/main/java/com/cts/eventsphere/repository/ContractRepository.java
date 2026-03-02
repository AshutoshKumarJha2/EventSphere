package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing and managing contract entities.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
}
