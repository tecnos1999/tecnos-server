package com.example.tecnosserver.testimonials.service;

import com.example.tecnosserver.testimonials.dto.TestimonialDTO;

import java.util.List;
import java.util.Optional;

public interface TestimonialQueryService {
    Optional<TestimonialDTO> findByCode(String code);
    Optional<List<TestimonialDTO>> findAllTestimonials();
}
