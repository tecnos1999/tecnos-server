package com.example.tecnosserver.testimonials.service;

import com.example.tecnosserver.testimonials.dto.TestimonialDTO;

public interface TestimonialCommandService {
    void addTestimonial(TestimonialDTO testimonialDTO);
    void deleteTestimonial(String code);

    void updateTestimonial(String code, TestimonialDTO testimonialDTO);
}
