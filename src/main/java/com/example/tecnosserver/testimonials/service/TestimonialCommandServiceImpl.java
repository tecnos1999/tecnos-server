package com.example.tecnosserver.testimonials.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.testimonials.dto.TestimonialDTO;
import com.example.tecnosserver.testimonials.mapper.TestimonialMapper;
import com.example.tecnosserver.testimonials.model.Testimonial;
import com.example.tecnosserver.testimonials.repo.TestimonialRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TestimonialCommandServiceImpl implements TestimonialCommandService {

    private final TestimonialRepo testimonialRepo;
    private final TestimonialMapper testimonialMapper;

    @Override
    public void addTestimonial(TestimonialDTO testimonialDTO) {
        validateTestimonialDTO(testimonialDTO);

        if (testimonialRepo.findByCode(testimonialDTO.code()).isPresent()) {
            throw new AlreadyExistsException("Testimonial with code '" + testimonialDTO.code() + "' already exists.");
        }

        Testimonial testimonial = testimonialMapper.fromDTO(testimonialDTO);
        testimonialRepo.save(testimonial);
    }

    @Override
    public void deleteTestimonial(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new AppException("Testimonial code cannot be null or empty.");
        }

        Testimonial testimonial = testimonialRepo.findByCode(code.trim())
                .orElseThrow(() -> new NotFoundException("Testimonial with code '" + code + "' not found."));

        testimonialRepo.delete(testimonial);
    }

    private void validateTestimonialDTO(TestimonialDTO testimonialDTO) {
        if (testimonialDTO == null) {
            throw new AppException("Testimonial data cannot be null.");
        }
        if (testimonialDTO.name() == null || testimonialDTO.name().trim().isEmpty()) {
            throw new AppException("Testimonial name cannot be null or empty.");
        }
        if (testimonialDTO.position() == null || testimonialDTO.position().trim().isEmpty()) {
            throw new AppException("Testimonial position cannot be null or empty.");
        }
        if (testimonialDTO.company() == null || testimonialDTO.company().trim().isEmpty()) {
            throw new AppException("Testimonial company cannot be null or empty.");
        }
        if (testimonialDTO.testimonial() == null || testimonialDTO.testimonial().trim().isEmpty()) {
            throw new AppException("Testimonial text cannot be null or empty.");
        }
    }
}
