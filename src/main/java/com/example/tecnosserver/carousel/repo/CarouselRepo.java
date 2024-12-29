package com.example.tecnosserver.carousel.repo;

import com.example.tecnosserver.carousel.model.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarouselRepo extends JpaRepository<Carousel, Long> {

    @Query("SELECT c FROM Carousel c WHERE c.type = 'image'")
    Optional<List<Carousel>> findAllImages();

    @Query("SELECT c FROM Carousel c WHERE c.type = 'video'")
    Optional<List<Carousel>> findAllVideos();

    @Query("SELECT c FROM Carousel c ORDER BY c.createdAt DESC")
    Optional<List<Carousel>> findAllOrderByCreatedAtDesc();

    Optional<Carousel> findByCode(String code);
}


