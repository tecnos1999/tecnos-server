package com.example.tecnosserver.products.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.subcategory.repo.SubCategoryRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepo productRepo;

    private final SubCategoryRepo subCategoryRepo;


    public ProductCommandServiceImpl(ProductRepo productRepo, SubCategoryRepo subCategoryRepo) {
        this.productRepo = productRepo;
        this.subCategoryRepo = subCategoryRepo;
    }

    @Override
    public void createProduct(ProductDTO productDTO) {
        Optional<Product> productOpt = productRepo.findProductBySku(productDTO.getSku());
        if (productOpt.isPresent()) {
            throw new AlreadyExistsException("Product with sku " + productDTO.getSku() + " already exists");
        }

        Optional<SubCategory> subCategoryOpt = subCategoryRepo.findSubCategoryByName(productDTO.getSubCategory());
        if (subCategoryOpt.isEmpty()) {
            throw new NotFoundException("Subcategory with name " + productDTO.getSubCategory() + " not found");
        }


        Product product = Product.builder()
                .sku(productDTO.getSku())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .image(productDTO.getImage())
                .subCategory(subCategoryOpt.get())
                .build();

        productRepo.save(product);
    }

    @Override
    public void updateProduct(String sku, ProductDTO updatedProductDTO) {
        Optional<Product> productOpt = productRepo.findProductBySku(sku);

        if (productOpt.isEmpty()) {
            throw new NotFoundException("Product with sku " + sku + " not found");
        }

        Optional<SubCategory> subCategoryOpt = subCategoryRepo.findSubCategoryByName(updatedProductDTO.getSubCategory());
        if (subCategoryOpt.isEmpty()) {
            throw new NotFoundException("Subcategory with name " + updatedProductDTO.getSubCategory() + " not found");
        }

        Product product = productOpt.get();


        product.setSku(updatedProductDTO.getSku());
        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setSubCategory(subCategoryOpt.get());
        product.setImage(updatedProductDTO.getImage());
        productRepo.save(product);
    }

    @Override
    public void deleteProduct(String sku) {
        Optional<Product> productOpt = productRepo.findProductBySku(sku);
        if (productOpt.isEmpty()) {
            throw new NotFoundException("Product with sku " + sku + " not found");
        }
        productRepo.delete(productOpt.get());
    }
}
