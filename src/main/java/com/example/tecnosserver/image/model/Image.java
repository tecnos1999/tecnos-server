package com.example.tecnosserver.image.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @SequenceGenerator(name = "image_sequence", sequenceName = "image_sequence", allocationSize = 1)
    @GeneratedValue(generator = "image_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;
}

