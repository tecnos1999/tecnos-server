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

    @Override
    public Optional<List<Product>> findAllByCategoryAndSubCategory(String category, String subCategory) {
        Optional<List<Product>> products = productRepo.findAllByCategoryAndSubCategory(category, subCategory);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for category " + category + " and subcategory " + subCategory);
        }
        return products;
    }

    @Override
    public Optional<List<Product>> findAllByCategoryAndSubCategoryAndItemCategory(String category, String subCategory, String itemCategory) {
        Optional<List<Product>> products = productRepo.findAllByCategoryAndSubCategoryAndItemCategory(category, subCategory, itemCategory);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for category " + category + ", subcategory " + subCategory + " and item category " + itemCategory);
        }
        return products;
    }


}
