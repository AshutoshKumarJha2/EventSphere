package com.cts.eventsphere.dto;

/**
 * DTO for Audit Log entity
 * * @author 2480010
 *
 * @version 1.0
 * @since 28-02-2026
 */
public record AuditLogResponseDto(
        String auditId,
        String userId,
        String action,
        String resource,
        String timeStamp,
        String createdAt,
        String updatedAt

) {
}
