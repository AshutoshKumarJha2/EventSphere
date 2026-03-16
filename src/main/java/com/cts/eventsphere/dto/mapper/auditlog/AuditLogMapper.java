package com.cts.eventsphere.dto.mapper.auditlog;

import com.cts.eventsphere.dto.audit.AuditResponseDTO;
import com.cts.eventsphere.model.AuditLog;

/**
 * AuditLogMapper is a utility class responsible for mapping AuditLog entities
 * to their respective Data Transfer Objects (DTOs).
 * * This class facilitates the decoupling of the internal database schema from
 * the external API responses.
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-08
 */
public class AuditLogMapper {

    /**
     * Converts an {@link AuditLog} entity into an {@link AuditResponseDTO}.
     * This method extracts user details and action metadata to provide a
     * flattened view for API consumers.
     *
     * @param audit The source {@link AuditLog} entity to be converted.
     * @return A constructed {@link AuditResponseDTO} containing the audit details.
     */
    public static AuditResponseDTO toDTO(AuditLog audit){
        return AuditResponseDTO.builder()
                .auditId(audit.getAuditId())
                .userId(audit.getUser().getUserId())
                .userName(audit.getUser().getName())
                .action(audit.getAction())
                .resource(audit.getResource())
                .entityId(audit.getEntityId())
                .entityName(audit.getEntityName())
                .build();
    }
}