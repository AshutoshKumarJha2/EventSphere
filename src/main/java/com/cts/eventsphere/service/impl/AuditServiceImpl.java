package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.audit.AuditListResponseDTO;
import com.cts.eventsphere.dto.mapper.auditlog.AuditLogMapper;
import com.cts.eventsphere.model.AuditLog;
import com.cts.eventsphere.model.User;
import com.cts.eventsphere.model.data.AuditAction;
import com.cts.eventsphere.repository.AuditLogRepository;
import com.cts.eventsphere.service.AuditService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * AuditServiceImpl provides the concrete implementation of the {@link AuditService}.
 * It handles the persistence of audit logs and facilitates paginated retrieval
 * of system activities for administrative oversight.
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-08
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository auditRepo;
    private final EntityManager entityManager;

    /**
     * Persists a new audit log entry.
     * Uses a proxy reference for the User entity to optimize performance.
     *
     * @param userId      The unique identifier of the user performing the action.
     * @param action      The type of action performed (e.g., CREATE, UPDATE, DELETE).
     * @param entityClass The class type of the entity being acted upon.
     * @param entityId    The unique identifier of the specific entity instance.
     */
    @Override
    public void logAudit(String userId, AuditAction action, Class<?> entityClass, String entityId) {
        var className = entityClass.getName();

        log.debug("Initiating audit log creation - User: {}, Action: {}, Entity: {}, EntityId: {}",
                userId, action, className, entityId);

        var userRef = entityManager.getReference(User.class, userId);

        var audit = AuditLog.builder()
                .user(userRef)
                .action(action.name())
                .resource(className)
                .entityId(entityId)
                .entityName(className)
                .timeStamp(LocalDateTime.now())
                .build();

        auditRepo.save(audit);
        log.info("Successfully saved audit log for EntityId: {}", entityId);
    }

    /**
     * Fetches a paginated list of audit records and maps them to DTOs.
     *
     * @param size The number of records per page.
     * @param page The page number to retrieve.
     * @return A {@link AuditListResponseDTO} containing paginated audit data and metadata.
     */
    @Override
    public AuditListResponseDTO getAudits(int size, int page) {
        var pageable = PageRequest.of(page, size);
        var pages = auditRepo.findAll(pageable);
        var audits = pages.stream().map(AuditLogMapper::toDTO).toList();

        var pageNo = pages.getNumber();
        var pageSize = pages.getSize();
        var totalElements = pages.getTotalElements();
        var totalPages = pages.getTotalPages();

        log.info("Fetched {} audits, page: {}, size: {}", audits.size(), page, size);
        return new AuditListResponseDTO(
                audits,
                pageNo,
                pageSize,
                totalElements,
                totalPages
        );
    }
}