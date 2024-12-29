package com.example.tecnosserver.carousel.service;

import com.example.tecnosserver.carousel.dto.CarouselDTO;

import java.util.List;
import java.util.Optional;

public interface CarouselQueryService {
    Optional<List<CarouselDTO>> getAllCarouselItems();

    Optional<List<CarouselDTO>> getAllImages();

    Optional<List<CarouselDTO>> getAllVideos();

    Optional<List<CarouselDTO>> getCarouselItemsOrderedByCreatedAt();
}
