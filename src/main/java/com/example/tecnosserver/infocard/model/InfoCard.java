package com.example.tecnosserver.infocard.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "info_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InfoCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "info_card_sequence")
    @SequenceGenerator(name = "info_card_sequence", sequenceName = "info_card_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "info_card_features", joinColumns = @JoinColumn(name = "info_card_id"))
    @Column(name = "feature", nullable = false)
    private List<String> features = new ArrayList<>();

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void generateCode() {
        if (this.code == null || this.code.isBlank()) {
            this.code = "IC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
    }

}

