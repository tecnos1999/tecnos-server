package com.example.tecnosserver.partners.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.awt.*;
import java.time.LocalDateTime;

@Entity(name = "Partner")
@Table(name = "partners")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Data
public class Partner {

    @Id
    @SequenceGenerator(name = "partner_sequence", sequenceName = "partner_sequence", allocationSize = 1)
    @GeneratedValue(generator = "partner_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "catalog_file")
    private String catalogFile;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Lob
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;
}
