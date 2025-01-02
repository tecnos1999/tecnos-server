package com.example.tecnosserver.caption.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @Column(name = "text", nullable = false, length = 2000)
    private String text;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "position", nullable = false)
    private String position;
}

