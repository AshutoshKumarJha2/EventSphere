package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.model.AuditLog;
import com.cts.eventsphere.model.data.AuditAction;
import com.cts.eventsphere.repository.AuditLogRepository;
import com.cts.eventsphere.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository auditRepo;

    @Override
    public void logAudit(String userId, AuditAction action, Class<?> entityClass, String entityId) {
        var className = entityClass.getName();

        log.debug("Initiating audit log creation - User: {}, Action: {}, Entity: {}, EntityId: {}",
                userId, action, className, entityId);

        var audit = AuditLog.builder()
                .userId(userId)
                .action(action.name())
                .resource(className)
                .entityId(entityId)
                .entityName(className)
                .build();

        auditRepo.save(audit);

        log.info("Successfully saved audit log for EntityId: {}", entityId);
    }
}