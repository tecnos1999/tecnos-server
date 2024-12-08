package com.example.tecnosserver.testimonials.mapper;


import com.example.tecnosserver.testimonials.dto.TestimonialDTO;
import com.example.tecnosserver.testimonials.model.Testimonial;
import org.springframework.stereotype.Component;

@Component
public class TestimonialMapper {

    public TestimonialDTO toDTO(Testimonial testimonial) {
        if (testimonial == null) {
            return null;
        }
        return TestimonialDTO.builder()
                .code(testimonial.getCode())
                .name(testimonial.getName())
                .position(testimonial.getPosition())
                .company(testimonial.getCompany())
                .testimonial(testimonial.getTestimonial())
                .build();
    }

    public Testimonial fromDTO(TestimonialDTO testimonialDTO) {
        if (testimonialDTO == null) {
            return null;
        }
        return Testimonial.builder()
                .code(testimonialDTO.code())
                .name(testimonialDTO.name())
                .position(testimonialDTO.position())
                .company(testimonialDTO.company())
                .testimonial(testimonialDTO.testimonial())
                .build();
    }
}

