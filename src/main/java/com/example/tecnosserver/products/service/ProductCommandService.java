package com.example.tecnosserver.products.service;

import com.example.tecnosserver.products.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductCommandService {

        void createProduct(ProductDTO productDTO, List<MultipartFile> imageFiles,
                                  MultipartFile broschureFile, MultipartFile tehnicFile);

        void updateProduct(String sku, ProductDTO updatedProductDTO,
                                  List<MultipartFile> newImageFiles,
                                  MultipartFile newBroschureFile, MultipartFile newTehnicFile);

        void deleteProduct(String sku);
}

