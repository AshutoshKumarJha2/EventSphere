package com.cts.eventsphere.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Feedback Model class
 *
 * @author 2480027
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "feedback")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", referencedColumnName = "eventId", insertable = false, updatable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendeeId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User attendee;

    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private String eventId;

    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private String attendeeId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String comments;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime date;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}