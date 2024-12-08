package com.example.tecnosserver.webinar.model;


import com.example.tecnosserver.image.model.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "Webinar")
@Table(name = "webinars")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Data
public class Webinar {

    @Id
    @SequenceGenerator(name = "webinar_sequence", sequenceName = "webinar_sequence", allocationSize = 1)
    @GeneratedValue(generator = "webinar_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "web_code", nullable = false, unique = true)
    private String webCode;

    @Column(name = "title", nullable = false)
    private String title;


    @Column(name = "external_link", nullable = false)
    private String externalLink;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @PrePersist
    protected void onCreate() {
        if (this.webCode == null || this.webCode.isEmpty()) {
            this.webCode = java.util.UUID.randomUUID().toString();
        }
    }
}

