package com.example.tecnosserver.caption.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "captions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Caption {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caption_sequence")
    @SequenceGenerator(name = "caption_sequence", sequenceName = "caption_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column(name = "text", nullable = false, length = 2000)
    private String text;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void generateCode() {
        if (this.code == null || this.code.isBlank()) {
            this.code = UUID.randomUUID().toString();
        }
    }
}
