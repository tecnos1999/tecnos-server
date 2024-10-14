package com.example.tecnosserver.products.service;

import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepo productRepo;


    public ProductCommandServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public void createProduct(ProductDTO productDTO) {



        Product product = Product.builder()
                .sku(productDTO.getSku())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .image(productDTO.getImage())
                .build();

        productRepo.save(product);
    }

    @Override
    public void updateProduct(String sku, ProductDTO updatedProductDTO) {
        Optional<Product> productOpt = productRepo.findProductBySku(sku);

        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Product product = productOpt.get();


        product.setSku(updatedProductDTO.getSku());
        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setImage(updatedProductDTO.getImage());

        productRepo.save(product);
    }

    @Override
    public void deleteProduct(String sku) {
        Optional<Product> productOpt = productRepo.findProductBySku(sku);

        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        productRepo.delete(productOpt.get());
    }
}
