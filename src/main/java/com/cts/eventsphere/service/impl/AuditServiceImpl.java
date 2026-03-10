package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.model.AuditLog;
import com.cts.eventsphere.model.data.AuditAction;
import com.cts.eventsphere.repository.AuditLogRepository;
import com.cts.eventsphere.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository auditRepo;
    @Override
    public void logAudit(String userId, AuditAction action, Class<?> entityClass, String entityId) {
        var className = entityClass.getName();
        var audit = AuditLog.builder()
                .userId(userId)
                .action(action.name())
                .resource(className)
                .entityId(entityId)
                .entityName(className)
                .build();
        auditRepo.save(audit);
    }
}
