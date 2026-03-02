package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.budget.BudgetRequestDto;
import com.cts.eventsphere.dto.budget.BudgetResponseDto;
import com.cts.eventsphere.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Budget Operations
 *
 * @author 2480081
 * @version 1.0
 * @since 01-03-2026
 */
@RestController
@RequestMapping("/api/events")
@Slf4j
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    /**
     * Setting Budget for an Event
     * @param eventId
     * @return "CREATED" response for successful Budget creation.
     */
    @PostMapping("/{eventId}/budget")
    public ResponseEntity<BudgetResponseDto> setBudget(@PathVariable String eventId , @RequestBody BudgetRequestDto request){
        return new ResponseEntity<>(budgetService.createBudget(eventId , request), HttpStatus.CREATED);
    }
}
