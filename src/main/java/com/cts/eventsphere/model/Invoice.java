package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents an invoice issued for contractual services.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "invoice")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String invoiceId;

    @Column(nullable = false)
    private String contractId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('issued','paid','overdue','cancelled')")
    private InvoiceStatus status = InvoiceStatus.issued;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
