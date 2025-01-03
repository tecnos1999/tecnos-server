package com.example.tecnosserver.carousel.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "carousel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Carousel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carousel_sequence")
    @SequenceGenerator(name = "carousel_sequence", sequenceName = "carousel_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "type", nullable = false)
    private String type;

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

