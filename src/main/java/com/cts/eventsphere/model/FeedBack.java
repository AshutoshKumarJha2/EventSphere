package com.cts.eventsphere.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * [ Detailed description of the class's responsibility]
 *
 * @author 2480027
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "feedback")
@Data

@NoArgsConstructor
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String feedbackId;

    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private String eventId;

    @Column(nullable = false,columnDefinition = "CHAR(36)")
    private String attendeeId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String comments;

    @Column(nullable = false)
    private LocalDateTime date;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
