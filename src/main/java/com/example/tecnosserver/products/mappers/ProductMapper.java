package com.example.tecnosserver.products.mappers;

import com.example.tecnosserver.image.dto.ImageDTO;
import com.example.tecnosserver.image.model.Image;
import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    /**
     * Map a Product entity to a ProductDTO.
     *
     * @param product the Product entity
     * @return the ProductDTO
     */
    public ProductDTO mapProductToDTO(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDTO(
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getItemCategory() != null ? product.getItemCategory().getName() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getSubCategory() != null ? product.getSubCategory().getName() : null,
                mapImagesToDTOs(product.getImages()),
                product.getBroschure(),
                product.getTehnic(),
                product.getCatalog(),
                product.getLinkVideo()
        );
    }

    /**
     * Map a list of Product entities to a list of ProductDTOs.
     *
     * @param products the list of Product entities
     * @return the list of ProductDTOs
     */
    public List<ProductDTO> mapProductsToDTOs(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }

        return products.stream()
                .map(this::mapProductToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Map a list of Image entities to a list of ImageDTOs.
     *
     * @param images the list of Image entities
     * @return the list of ImageDTOs
     */
    private List<ImageDTO> mapImagesToDTOs(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        return images.stream()
                .map(image -> new ImageDTO(image.getUrl(), image.getType()))
                .collect(Collectors.toList());
    }
}
