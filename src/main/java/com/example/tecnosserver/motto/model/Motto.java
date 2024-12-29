package com.example.tecnosserver.motto.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "motto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Motto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "motto_sequence")
    @SequenceGenerator(name = "motto_sequence", sequenceName = "motto_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column(name = "content", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void generateCode() {
        if (this.code == null || this.code.isBlank()) {
            this.code = UUID.randomUUID().toString();
        }
    }
}
