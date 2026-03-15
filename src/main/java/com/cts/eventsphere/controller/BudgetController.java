package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.budget.BudgetRequestDto;
import com.cts.eventsphere.dto.budget.BudgetResponseDto;
import com.cts.eventsphere.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling Budget-related operations in the EventSphere application.
 *
 * <p>This REST controller exposes endpoints for managing budgets associated
 * with events. It delegates business logic to the {@link BudgetService} and
 * ensures proper request validation, authorization, and response formatting.</p>
 *
 * <p>Security is enforced using {@code @PreAuthorize}, allowing only users
 * with roles ADMIN, ORGANIZER, or FINANCE_MANAGER to access budget operations.</p>
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@RestController
@RequestMapping("/api/events/v1")
@Slf4j
@RequiredArgsConstructor

public class BudgetController {

    private final BudgetService budgetService;

    /**
     * Sets the budget for a specific event.
     *
     * <p>This endpoint accepts a validated {@link BudgetRequestDto} in the request body
     * and associates it with the event identified by {@code eventId}. The service layer
     * handles the creation logic and returns a {@link BudgetResponseDto} containing
     * the budget details.</p>
     *
     * <p>Authorization is restricted to users with roles ADMIN, ORGANIZER, or FINANCE_MANAGER.
     * On success, the response includes the created budget and HTTP status {@code CREATED} (201).</p>
     *
     * @param eventId the unique identifier of the event for which the budget is being set
     * @param request the validated budget request payload containing budget details
     * @return a ResponseEntity containing the created {@link BudgetResponseDto} and
     *         HTTP status {@code CREATED}
     */
    @PostMapping("/{eventId}/budget")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'FINANCE_MANAGER')")
    public ResponseEntity<BudgetResponseDto> setBudget(@PathVariable String eventId ,@Valid @RequestBody BudgetRequestDto request){
        log.info("Request to set budget for eventId: {} with data: {}", eventId, request);
        BudgetResponseDto response = budgetService.createBudget(eventId , request);
        log.info("Response for eventId: {}: {}", eventId, response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
