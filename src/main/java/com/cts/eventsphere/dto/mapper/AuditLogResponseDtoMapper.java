package com.cts.eventsphere.dto.mapper;

import com.cts.eventsphere.dto.AuditLogResponseDto;
import com.cts.eventsphere.model.AuditLog;
import org.springframework.stereotype.Component;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2480010
 *
 * @version 1.0
 * @since 28-02-2026
 */
@Component
public class AuditLogResponseDtoMapper {
    public AuditLogResponseDto toDTO(AuditLog auditLog){
        return new AuditLogResponseDto(
                auditLog.getAuditId(),
                auditLog.getUserId(),
                auditLog.getAction(),
                auditLog.getResource(),
                auditLog.getTimeStamp().toString(),
                auditLog.getCreatedAt().toString(),
                auditLog.getUpdatedAt().toString()
        );
    }
}
