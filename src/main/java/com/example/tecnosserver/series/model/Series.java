package com.example.tecnosserver.series.model;


import com.example.tecnosserver.blog.model.Blog;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "series")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_sequence")
    @SequenceGenerator(name = "series_sequence", sequenceName = "series_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY)
    private List<Blog> blogs = new ArrayList<>();

    @PrePersist
    protected void generateCode() {
        if (this.code == null || this.code.isBlank()) {
            this.code = UUID.randomUUID().toString();
        }
    }

}
