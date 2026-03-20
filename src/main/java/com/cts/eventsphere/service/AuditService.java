package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.audit.AuditListResponseDTO;
import com.cts.eventsphere.model.data.AuditAction;

/**
 * AuditService interface defines the contract for logging and retrieving audit trails.
 * It provides methods to track user actions across various entities within the system.
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-08
 */
public interface AuditService {

    /**
     * Logs a specific action performed by a user on a system entity.
     *
     * @param userId      The unique identifier of the user performing the action.
     * @param action      The type of action performed (e.g., CREATE, UPDATE, DELETE).
     * @param entityClass The class type of the entity being acted upon.
     * @param entityId    The unique identifier of the specific entity instance.
     */

    public void logAudit(
            String userId,
            AuditAction action,
            Class<?> entityClass,
            String entityId
    );

    /**
     * Retrieves a paginated list of all audit records stored in the system.
     *
     * @param size The number of records per page.
     * @param page The page number to retrieve.
     * @return An {@link AuditListResponseDTO} containing the requested page of audit logs.
     */
    public AuditListResponseDTO getAudits(int size, int page);
}