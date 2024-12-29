package com.example.tecnosserver.carousel.service;


import com.example.tecnosserver.carousel.dto.CarouselDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CarouselCommandService {
    void addCarouselItem(CarouselDTO carouselDTO, MultipartFile file);

    void updateCarouselItem(String code, CarouselDTO carouselDTO, MultipartFile file);

    void deleteCarouselItem(String code);
}

