package com.cts.eventsphere.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * This Entity represents financial allocation of an Event.
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
@Entity
@Table(name = "budget")
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String budgetId;

    @Column(nullable = false , columnDefinition = "CHAR(36)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId" , nullable = false)
    private Event event;

    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal plannedAmount;

    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal actualAmount = BigDecimal.ZERO; // There is no actualAmount in the starting so it should start from 0

    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal variance = BigDecimal.ZERO; // (variance = plannedAmount - actualAmount) ---> so initially it will be 0

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;




}
