package com.example.tecnosserver.testimonials.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.testimonials.dto.TestimonialDTO;
import com.example.tecnosserver.testimonials.mapper.TestimonialMapper;
import com.example.tecnosserver.testimonials.model.Testimonial;
import com.example.tecnosserver.testimonials.repo.TestimonialRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestimonialQueryServiceImpl implements TestimonialQueryService {

    private final TestimonialRepo testimonialRepo;
    private final TestimonialMapper testimonialMapper;

    @Override
    public Optional<TestimonialDTO> findByCode(String code) {
        return testimonialRepo.findByCode(code)
                .map(testimonialMapper::toDTO);
    }

    @Override
    public Optional<List<TestimonialDTO>> findAllTestimonials() {
        List<Testimonial> testimonials = testimonialRepo.findAll();
        if (testimonials.isEmpty()) {
            throw new NotFoundException("No testimonials found.");
        }

        List<TestimonialDTO> testimonialDTOs = testimonials.stream()
                .map(testimonialMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(testimonialDTOs);
    }
}
