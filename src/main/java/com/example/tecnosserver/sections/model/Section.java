package com.example.tecnosserver.sections.model;


import com.example.tecnosserver.page.model.Page;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "section_sequence")
    @SequenceGenerator(name = "section_sequence", sequenceName = "section_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name="position")
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;
}

