package com.example.tecnosserver.products.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.itemcategory.repo.ItemCategoryRepo;
import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import com.example.tecnosserver.subcategory.model.SubCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepo productRepo;

    private final ItemCategoryRepo itemCategoryRepo;

    public ProductCommandServiceImpl(ProductRepo productRepo, ItemCategoryRepo itemCategoryRepo) {
        this.productRepo = productRepo;
        this.itemCategoryRepo = itemCategoryRepo;
    }


    @Override
    public void createProduct(ProductDTO productDTO) {
        Optional<Product> productOpt = productRepo.findProductBySku(productDTO.getSku());
        if (productOpt.isPresent()) {
            throw new AlreadyExistsException("Product with sku " + productDTO.getSku() + " already exists");
        }



        Optional<ItemCategory> itemCategoryOpt = itemCategoryRepo.findByName(productDTO.getItemCategory());
        if (itemCategoryOpt.isEmpty()) {
            throw new NotFoundException("Item category with name " + productDTO.getItemCategory() + " not found");
        }


        Product product = Product.builder()
                .sku(productDTO.getSku())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .image(productDTO.getImage())
                .itemCategory(itemCategoryOpt.get())
                .build();

        productRepo.save(product);
    }

    @Override
    public void updateProduct(String sku, ProductDTO updatedProductDTO) {
        Optional<Product> productOpt = productRepo.findProductBySku(sku);

        if (productOpt.isEmpty()) {
            throw new NotFoundException("Product with sku " + sku + " not found");
        }


        Optional<ItemCategory> itemCategoryOpt = itemCategoryRepo.findByName(updatedProductDTO.getItemCategory());
        if (itemCategoryOpt.isEmpty()) {
            throw new NotFoundException("Item category with name " + updatedProductDTO.getItemCategory() + " not found");
        }

        Product product = productOpt.get();


        product.setSku(updatedProductDTO.getSku());
        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setItemCategory(itemCategoryOpt.get());
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
