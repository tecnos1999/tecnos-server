package com.example.tecnosserver.products.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.itemcategory.repo.ItemCategoryRepo;
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
    private final ItemCategoryRepo itemCategoryRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;

    public ProductCommandServiceImpl(ProductRepo productRepo, ItemCategoryRepo itemCategoryRepo,
                                     CategoryRepo categoryRepo, SubCategoryRepo subCategoryRepo) {
        this.productRepo = productRepo;
        this.itemCategoryRepo = itemCategoryRepo;
        this.categoryRepo = categoryRepo;
        this.subCategoryRepo = subCategoryRepo;
    }

    @Override
    public void createProduct(ProductDTO productDTO) {
        Optional<Product> productOpt = productRepo.findProductBySku(productDTO.getSku());
        if (productOpt.isPresent()) {
            throw new AlreadyExistsException("Product with sku " + productDTO.getSku() + " already exists");
        }

        Optional<ItemCategory> itemCategoryOpt = Optional.empty();
        if (productDTO.getItemCategory() != null) {
            itemCategoryOpt = itemCategoryRepo.findByName(productDTO.getItemCategory());
            if (itemCategoryOpt.isEmpty()) {
                throw new NotFoundException("Item category with name " + productDTO.getItemCategory() + " not found");
            }
        }

        Optional<Category> categoryOpt = Optional.empty();
        if (productDTO.getCategory() != null) {
            categoryOpt = categoryRepo.findCategoryByName(productDTO.getCategory());
            if (categoryOpt.isEmpty()) {
                throw new NotFoundException("Category with name " + productDTO.getCategory() + " not found");
            }
        }

        Optional<SubCategory> subCategoryOpt = Optional.empty();
        if (productDTO.getSubCategory() != null) {
            subCategoryOpt = subCategoryRepo.findSubCategoryByName(productDTO.getSubCategory());
            if (subCategoryOpt.isEmpty()) {
                throw new NotFoundException("Subcategory with name " + productDTO.getSubCategory() + " not found");
            }
        }

        Product product = Product.builder()
                .sku(productDTO.getSku())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .itemCategory(itemCategoryOpt.orElse(null))
                .category(categoryOpt.orElse(null))
                .subCategory(subCategoryOpt.orElse(null))
                .broschure(productDTO.getBroschure())
                .tehnic(productDTO.getTehnic())
                .catalog(productDTO.getCatalog())
                .linkVideo(productDTO.getLinkVideo())
                .build();

        productRepo.save(product);
    }

    @Override
    public void updateProduct(String sku, ProductDTO updatedProductDTO) {
        Optional<Product> productOpt = productRepo.findProductBySku(sku);
        if (productOpt.isEmpty()) {
            throw new NotFoundException("Product with sku " + sku + " not found");
        }

        Optional<ItemCategory> itemCategoryOpt = Optional.empty();
        if (updatedProductDTO.getItemCategory() != null) {
            itemCategoryOpt = itemCategoryRepo.findByName(updatedProductDTO.getItemCategory());
            if (itemCategoryOpt.isEmpty()) {
                throw new NotFoundException("Item category with name " + updatedProductDTO.getItemCategory() + " not found");
            }
        }

        Optional<Category> categoryOpt = Optional.empty();
        if (updatedProductDTO.getCategory() != null) {
            categoryOpt = categoryRepo.findCategoryByName(updatedProductDTO.getCategory());
            if (categoryOpt.isEmpty()) {
                throw new NotFoundException("Category with name " + updatedProductDTO.getCategory() + " not found");
            }
        }

        Optional<SubCategory> subCategoryOpt = Optional.empty();
        if (updatedProductDTO.getSubCategory() != null) {
            subCategoryOpt = subCategoryRepo.findSubCategoryByName(updatedProductDTO.getSubCategory());
            if (subCategoryOpt.isEmpty()) {
                throw new NotFoundException("Subcategory with name " + updatedProductDTO.getSubCategory() + " not found");
            }
        }

        Product product = getProduct(updatedProductDTO, productOpt, itemCategoryOpt, categoryOpt, subCategoryOpt);
        productRepo.save(product);
    }

    private static Product getProduct(ProductDTO updatedProductDTO, Optional<Product> productOpt,
                                      Optional<ItemCategory> itemCategoryOpt, Optional<Category> categoryOpt,
                                      Optional<SubCategory> subCategoryOpt) {
        Product product = productOpt.get();
        product.setSku(updatedProductDTO.getSku());
        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setItemCategory(itemCategoryOpt.orElse(null));
        product.setCategory(categoryOpt.orElse(null));
        product.setSubCategory(subCategoryOpt.orElse(null));
        product.setBroschure(updatedProductDTO.getBroschure());
        product.setTehnic(updatedProductDTO.getTehnic());
        product.setCatalog(updatedProductDTO.getCatalog());
        product.setLinkVideo(updatedProductDTO.getLinkVideo());
        return product;
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
