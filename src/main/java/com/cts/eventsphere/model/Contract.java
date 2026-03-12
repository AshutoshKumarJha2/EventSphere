package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.ContractStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a contract between EventSphere and a vendor/client.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "contract")
@Data
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contractId", columnDefinition = "CHAR(36)")
    private String contractId;

    @Column(name = "vendorId", columnDefinition = "CHAR(36)")
    private String vendorId;

    @Column(name = "eventId", columnDefinition = "CHAR(36)")
    private String eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendorId", insertable = false, updatable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    private Event event;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('draft','active','completed','terminated')")
    private ContractStatus status = ContractStatus.draft;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
        invoice.setContract(this);
        invoice.setContractId(this.contractId);
    }

    public void removeInvoice(Invoice invoice) {
        invoices.remove(invoice);
        invoice.setContract(null);
    }
}