package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "invoiceId", columnDefinition = "CHAR(36)")
    private String invoiceId;

    @OneToMany(mappedBy = "invoice" , cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();


    @Column(nullable = false)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId", insertable = false, updatable = false)
    private Contract contract;

    @Column
    private LocalDateTime issueDate;

    private LocalDateTime dueDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('issued','paid','overdue','cancelled')")
    private InvoiceStatus status = InvoiceStatus.issued;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        delivery.setInvoice(this);
        delivery.setInvoiceId(this.invoiceId);
    }

    public void removeDelivery(Delivery delivery) {
        deliveries.remove(delivery);
        delivery.setInvoice(null);
    }
}