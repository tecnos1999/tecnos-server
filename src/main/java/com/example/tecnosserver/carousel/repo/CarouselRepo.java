package com.example.tecnosserver.carousel.repo;

import com.example.tecnosserver.carousel.model.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarouselRepo extends JpaRepository<Carousel, Long> {
}

