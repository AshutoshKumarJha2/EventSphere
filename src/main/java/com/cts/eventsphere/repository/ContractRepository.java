package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * JPA Repository interface for CRUD operations and custom queries on contract entities.
 * Handles the persistence of legal agreements between organizers and vendors.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

}

