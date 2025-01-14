package com.example.tecnosserver.products.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.mapper.ProductMapper;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    public ProductQueryServiceImpl(ProductRepo productRepo, ProductMapper productMapper) {
        this.productRepo = productRepo;
        this.productMapper = productMapper;
    }

    @Override
    public Optional<ProductDTO> findProductBySku(String sku) {
        Optional<Product> product = productRepo.findProductBySku(sku);
        if (product.isEmpty()) {
            throw new NotFoundException("Product with sku " + sku + " not found");
        }
        return product.map(productMapper::mapProductToDTO);
    }

    @Override
    public Optional<List<ProductDTO>> findAllProducts() {
        List<Product> products = productRepo.findAll();
        if (products.isEmpty()) {
            throw new NotFoundException("No products found");
        }
        return Optional.of(productMapper.mapProductsToDTOs(products));
    }

    @Override
    public Optional<List<ProductDTO>> findAllByCategoryAndSubCategory(String category, String subCategory) {
        Optional<List<Product>> products = productRepo.findAllByCategoryAndSubCategory(category, subCategory);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for category " + category + " and subcategory " + subCategory);
        }
        return products.map(productMapper::mapProductsToDTOs);
    }

    @Override
    public Optional<List<ProductDTO>> findAllByCategoryAndSubCategoryAndItemCategory(String category, String subCategory, String itemCategory) {
        Optional<List<Product>> products = productRepo.findAllByCategoryAndSubCategoryAndItemCategory(category, subCategory, itemCategory);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for category " + category + ", subcategory " + subCategory + " and item category " + itemCategory);
        }
        return products.map(productMapper::mapProductsToDTOs);
    }

    @Override
    public Optional<List<ProductDTO>> findAllByPartnerName(String partnerName) {
        Optional<List<Product>> products = productRepo.findAllByPartnerName(partnerName);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for partner " + partnerName);
        }
        return products.map(productMapper::mapProductsToDTOs);
    }

    @Override
    public Optional<List<ProductDTO>> findAllByTagName(String tagName) {
        Optional<List<Product>> products = productRepo.findAllByTagName(tagName);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for tag " + tagName);
        }
        return products.map(productMapper::mapProductsToDTOs);
    }

    @Override
    public Optional<List<ProductDTO>> findAllByTagsName(List<String> tagNames) {
        Optional<List<Product>> products = productRepo.findAllByTagsName(tagNames);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for tags " + String.join(", ", tagNames));
        }
        return products.map(productMapper::mapProductsToDTOs);
    }

    @Override
    public List<ProductDTO> findBySkuIn(List<String> skus) {
        if (skus == null || skus.isEmpty()) {
            throw new NotFoundException("SKU list cannot be null or empty.");
        }

        log.info("Searching for products with SKUs: {}", skus);
        List<Product> products = productRepo.findBySkuIn(skus);

        if (products.isEmpty()) {
            log.warn("No products found for SKUs: {}", skus);
            throw new NotFoundException("No products found for SKUs: " + String.join(", ", skus));
        }

        log.info("Found {} products for SKUs: {}", products.size(), skus);
        return productMapper.mapProductsToDTOs(products);
    }

}
