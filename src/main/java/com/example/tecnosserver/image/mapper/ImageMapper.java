package com.example.tecnosserver.image.mapper;
import com.example.tecnosserver.image.dto.ImageDTO;
import com.example.tecnosserver.image.model.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageMapper {

    /**
     * Map a single Image entity to an ImageDTO.
     *
     * @param image the Image entity
     * @return the ImageDTO
     */
    public ImageDTO mapImageToDTO(Image image) {
        if (image == null) {
            return null;
        }

        return new ImageDTO(image.getUrl(), image.getType());
    }

    /**
     * Map a list of Image entities to a list of ImageDTOs.
     *
     * @param images the list of Image entities
     * @return the list of ImageDTOs
     */
    public List<ImageDTO> mapImagesToDTOs(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        return images.stream()
                .map(this::mapImageToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Map a single ImageDTO to an Image entity.
     *
     * @param imageDTO the ImageDTO
     * @return the Image entity
     */
    public static Image mapDTOToImage(ImageDTO imageDTO) {
        if (imageDTO == null) {
            return null;
        }

        return Image.builder()
                .url(imageDTO.getUrl())
                .type(imageDTO.getType())
                .build();
    }
}
