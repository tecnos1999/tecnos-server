package com.example.tecnosserver.products.service;

import com.example.tecnosserver.products.dto.ProductDTO;

public interface ProductCommandService {

        void createProduct(ProductDTO productDTO);

        void updateProduct(String sku, ProductDTO updatedProductDTO);

        void deleteProduct(String sku);
}

