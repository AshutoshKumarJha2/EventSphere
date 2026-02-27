package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.ExpenseStatus;
import com.cts.eventsphere.model.data.PaymentMethod;
import com.cts.eventsphere.model.data.PaymentStatus;
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
 * Payment Entity representing financial transactions on the invoice generated.
 * Tracks Payment method and Status.
 *
 * @author 2480081
 * @version 1.0
 * @since 26-02-2026
 */
@Entity
@Table(name = "payment")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String paymentId;

    @Column(nullable = false , columnDefinition = "CHAR(36)")
    private String invoiceId;

    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , columnDefinition = "ENUM('credit_card', 'debit_card', 'bank_transfer', 'cash' , 'upi' , 'paypal')")
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('pending', 'completed', 'failed', 'refunded')")
    private PaymentStatus status = PaymentStatus.pending;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
