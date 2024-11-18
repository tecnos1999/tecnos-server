package com.example.tecnosserver.products.service;

import com.example.tecnosserver.products.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductQueryService {

    Optional<Product> findProductBySku(String sku);

    Optional<List<Product>> findAllProducts();

    Optional<List<Product>> findAllByCategoryAndSubCategory(String category, String subCategory);

    Optional<List<Product>> findAllByCategoryAndSubCategoryAndItemCategory(String category, String subCategory, String itemCategory);

}
