package com.example.tecnosserver.products.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductQueryServiceImpl implements ProductQueryService{

    private final ProductRepo productRepo;

    public ProductQueryServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Optional<Product> findProductBySku(String sku) {
        Optional<Product> product = productRepo.findProductBySku(sku);
        if (product.isEmpty()) {
            throw new NotFoundException("Product with sku " + sku + " not found");
        }
        return product;
    }

    @Override
    public Optional<List<Product>> findAllProducts() {
        List<Product> products = productRepo.findAll();
        if (products.isEmpty()) {
            throw new NotFoundException("No products found");
        }
        return Optional.of(products);
    }


}
