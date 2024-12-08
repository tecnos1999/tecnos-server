package com.example.tecnosserver.testimonials.dto;

import lombok.Builder;

@Builder
public record TestimonialDTO(
        String code,
        String name,
        String position,
        String company,
        String testimonial
) {}

