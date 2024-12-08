package com.example.tecnosserver.testimonials.web;

import com.example.tecnosserver.testimonials.dto.TestimonialDTO;
import com.example.tecnosserver.testimonials.service.TestimonialCommandService;
import com.example.tecnosserver.testimonials.service.TestimonialQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/testimonials")
@RequiredArgsConstructor
public class TestimonialControllerApi {

    private final TestimonialCommandService testimonialCommandService;
    private final TestimonialQueryService testimonialQueryService;

    @PostMapping("/create")
    public ResponseEntity<String> addTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
        testimonialCommandService.addTestimonial(testimonialDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Testimonial added successfully.");
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteTestimonial(@PathVariable String code) {
        testimonialCommandService.deleteTestimonial(code);
        return ResponseEntity.ok("Testimonial deleted successfully.");
    }

    @GetMapping("/{code}")
    public ResponseEntity<TestimonialDTO> getTestimonialByCode(@PathVariable String code) {
        return testimonialQueryService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<TestimonialDTO>> getAllTestimonials() {
        Optional<List<TestimonialDTO>> testimonials = testimonialQueryService.findAllTestimonials();
        return testimonials.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
