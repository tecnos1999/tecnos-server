package com.example.tecnosserver.carousel.mapper;

import com.example.tecnosserver.carousel.dto.CarouselDTO;
import com.example.tecnosserver.carousel.model.Carousel;
import org.springframework.stereotype.Component;

@Component
public class CarouselMapper {

    public Carousel fromDTO(CarouselDTO dto) {
        return Carousel.builder()
                .fileUrl(dto.fileUrl())
                .type(dto.type())
                .build();
    }

    public CarouselDTO toDTO(Carousel carousel) {
        return new CarouselDTO(
                carousel.getCode(),
                carousel.getFileUrl(),
                carousel.getType(),
                carousel.getCreatedAt(),
                carousel.getUpdatedAt()
        );
    }
}
