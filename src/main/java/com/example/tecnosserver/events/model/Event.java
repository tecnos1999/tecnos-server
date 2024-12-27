package com.example.tecnosserver.events.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "Event")
@Table(name = "events")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Data
public class Event {

    @Id
    @SequenceGenerator(name = "event_sequence", sequenceName = "event_sequence", allocationSize = 1)
    @GeneratedValue(generator = "event_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "event_code", nullable = false, unique = true)
    private String eventCode;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Lob
    private String description;

    @Column(name = "external_link", nullable = false)
    private String externalLink;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @PrePersist
    protected void onCreate() {
        if (this.eventCode == null || this.eventCode.isEmpty()) {
            this.eventCode = java.util.UUID.randomUUID().toString();
        }
    }
}

