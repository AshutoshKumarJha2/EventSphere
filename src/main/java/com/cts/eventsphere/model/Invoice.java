package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a financial billing record.
 * Tracks the amount due, status, and associated deliverables for a contract.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "invoiceId", columnDefinition = "CHAR(36)")
    private String invoiceId;

    @OneToMany(mappedBy = "invoice" , cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "contractId", columnDefinition = "CHAR(36)",nullable = false)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId", insertable = false, updatable = false)
    private Contract contract;

    @CreationTimestamp
    @Column(name = "issueDate", updatable = false)
    private LocalDateTime issueDate;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('issued','paid','overdue','cancelled')")
    private InvoiceStatus status = InvoiceStatus.issued;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Links a new delivery item to this invoice.
     *
     * @param delivery the delivery entity to be associated
     */
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        delivery.setInvoice(this);
        delivery.setInvoiceId(this.invoiceId);
    }

    /**
     * Records a new payment against this invoice.
     *
     * @param payment the payment details to be added
     */
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setInvoice(this);
    }
}