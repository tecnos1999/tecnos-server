package com.example.tecnosserver.products.mapper;

import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.tags.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

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
                product.getImages() != null
                        ? product.getImages().stream().map(image -> image.getImageUrl()).toList()
                        : List.of(),
                null, // imagesToKeep
                null, // imagesToRemove
                product.getBroschure(),
                product.getTehnic(),
                product.getLinkVideo(),
                product.getPartner() != null ? product.getPartner().getName() : null,
                product.getTags() != null
                        ? product.getTags().stream().map(Tag::getName).toList()
                        : List.of()
        );
    }

    public Product fromDTO(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setBroschure(productDTO.getBroschure());
        product.setTehnic(productDTO.getTehnic());
        product.setLinkVideo(productDTO.getLinkVideo());

        if (productDTO.getImages() != null) {
            product.addImages(productDTO.getImages());
        }


        return product;
    }

    public List<ProductDTO> mapProductsToDTOs(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }

        return products.stream()
                .map(this::mapProductToDTO)
                .toList();
    }

    public List<Product> fromDTOs(List<ProductDTO> productDTOs) {
        if (productDTOs == null || productDTOs.isEmpty()) {
            return List.of();
        }

        return productDTOs.stream()
                .map(this::fromDTO)
                .toList();
    }
}
