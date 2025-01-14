package com.example.tecnosserver.products.service;

import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductQueryService {

    Optional<ProductDTO> findProductBySku(String sku);

    Optional<List<ProductDTO>> findAllProducts();

    Optional<List<ProductDTO>> findAllByCategoryAndSubCategory(String category, String subCategory);

    Optional<List<ProductDTO>> findAllByCategoryAndSubCategoryAndItemCategory(String category, String subCategory, String itemCategory);

    Optional<List<ProductDTO>> findAllByPartnerName(String partnerName);

    Optional<List<ProductDTO>> findAllByTagName(String tagName);

    Optional<List<ProductDTO>> findAllByTagsName(List<String> tagNames);

    List<ProductDTO> findBySkuIn(List<String> skus);
}
