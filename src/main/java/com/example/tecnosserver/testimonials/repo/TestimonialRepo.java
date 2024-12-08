package com.example.tecnosserver.testimonials.repo;


import com.example.tecnosserver.testimonials.model.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestimonialRepo extends JpaRepository<Testimonial, Long> {
    
    Optional<Testimonial> findByCode(String code);
}

