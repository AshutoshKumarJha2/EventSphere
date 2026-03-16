package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository interface for persisting and querying invoice entities.
 * Provides abstraction for the persistence layer, enabling financial record
 * management and contract-linked billing queries.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    /**
     * Finds an invoice by the contract ID.
     * * @param contractId the unique identifier of the associated contract
     * @return an Optional containing the invoice if found, or empty otherwise
     */
    Optional<Invoice> findByContractId(String contractId);
}
