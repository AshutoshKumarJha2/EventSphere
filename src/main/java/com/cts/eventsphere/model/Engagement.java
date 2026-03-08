package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.EngagementType;
import com.cts.eventsphere.model.data.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Engagement Model class
 *
 * @author 2480027
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "engagement")
@Data
public class Engagement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String engagementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", referencedColumnName = "eventId", insertable = false, updatable = false)
    private Event event;

    @Column(name = "eventId", nullable = false, columnDefinition = "CHAR(36)")
    private String eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendeeId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User attendee;

    @Column(name = "attendeeId", nullable = false, columnDefinition = "CHAR(36)")
    private String attendeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EngagementType activity;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}