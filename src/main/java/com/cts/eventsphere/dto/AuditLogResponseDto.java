package com.cts.eventsphere.dto;

/**
 * [Detailed description of the class's responsibility]
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
