package com.example.tecnosserver.testimonials.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "Testimonial")
@Table(name = "testimonials")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Data
public class Testimonial {
    @Id
    @SequenceGenerator(name = "testimonial_sequence", sequenceName = "testimonial_sequence", allocationSize = 1)
    @GeneratedValue(generator = "testimonial_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "test_code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "company")
    private String company;

    @Column(name = "testimonial", nullable = false, columnDefinition = "TEXT")
    @Lob
    private String testimonial;


}
