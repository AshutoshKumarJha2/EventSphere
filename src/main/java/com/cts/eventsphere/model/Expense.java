package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.EventStatus;
import com.cts.eventsphere.model.data.ExpenseStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Expense entity representing  financial transactions which is connected to an Event.
 * This entity also needs an approval along with its Status.
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
@Entity
@Table(name = "expense")
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String expenseId;

    @Column(nullable = false , columnDefinition = "CHAR(36)")
    private String eventId;

    @Column(nullable = false , length = 255)
    private String description;

    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(columnDefinition = "CHAR(36)")
    private String approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('submitted', 'rejected', 'approved', 'paid')")
    private ExpenseStatus status = ExpenseStatus.submitted;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
