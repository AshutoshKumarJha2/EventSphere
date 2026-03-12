package com.cts.eventsphere.service;

import com.cts.eventsphere.model.data.AuditAction;

public interface AuditService {
    public void logAudit(
            String userId,
            AuditAction action,
            Class<?> entityClass,
            String entityId
    );
}
