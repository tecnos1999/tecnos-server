package com.example.tecnosserver.products.mapper;

import com.example.tecnosserver.productimage.model.ProductImage;
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
                        ? product.getImages().stream().map(ProductImage::getImageUrl).toList()
                        : List.of(),
                null,
                null,
                product.getBroschure(),
                product.getTehnic(),
                product.getLinkVideo(),
                product.getPartner() != null ? product.getPartner().getName() : null,
                product.getTags() != null
                        ? product.getTags().stream().map(Tag::getName).toList()
                        : List.of()
        );
    }


    public List<ProductDTO> mapProductsToDTOs(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }

        return products.stream()
                .map(this::mapProductToDTO)
                .toList();
    }

}
