package com.example.tecnosserver.products.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.image.model.Image;
import com.example.tecnosserver.intercom.CloudAdapter;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.itemcategory.repo.ItemCategoryRepo;
import com.example.tecnosserver.partners.model.Partner;
import com.example.tecnosserver.partners.repo.PartnerRepo;
import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.subcategory.repo.SubCategoryRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepo productRepo;
    private final ItemCategoryRepo itemCategoryRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;

    private final PartnerRepo partnerRepo;

    private final CloudAdapter cloudAdapter;

    public ProductCommandServiceImpl(ProductRepo productRepo, ItemCategoryRepo itemCategoryRepo,
                                     CategoryRepo categoryRepo, SubCategoryRepo subCategoryRepo, PartnerRepo partnerRepo, CloudAdapter cloudAdapter) {
        this.productRepo = productRepo;
        this.itemCategoryRepo = itemCategoryRepo;
        this.categoryRepo = categoryRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.partnerRepo = partnerRepo;
        this.cloudAdapter = cloudAdapter;
    }

    @Override
    public void createProduct(ProductDTO productDTO) {
        validateMandatoryFields(productDTO);

        if (productRepo.findProductBySku(productDTO.getSku()).isPresent()) {
            throw new AlreadyExistsException("Product with SKU '" + productDTO.getSku() + "' already exists");
        }

        Optional<Category> categoryOpt = validateAndRetrieveCategory(productDTO.getCategory());
        Optional<SubCategory> subCategoryOpt = validateAndRetrieveSubCategory(productDTO.getSubCategory(), productDTO.getCategory());
        Optional<ItemCategory> itemCategoryOpt = validateAndRetrieveItemCategory(productDTO.getItemCategory(), productDTO.getSubCategory(), productDTO.getCategory());

        Partner partner = null;
        if (productDTO.getPartnerName() != null) {
            partner = partnerRepo.findByName(productDTO.getPartnerName())
                    .orElseThrow(() -> new NotFoundException("Partner with name '" + productDTO.getPartnerName() + "' not found"));
        }

        Product product = Product.builder()
                .sku(productDTO.getSku())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .category(categoryOpt.orElse(null))
                .subCategory(subCategoryOpt.orElse(null))
                .itemCategory(itemCategoryOpt.orElse(null))
                .broschure(productDTO.getBroschure())
                .tehnic(productDTO.getTehnic())
                .catalog(productDTO.getCatalog())
                .linkVideo(productDTO.getLinkVideo())
                .partner(partner)
                .build();

        if (productDTO.getImages() != null && !productDTO.getImages().isEmpty()) {
            product.setImages(productDTO.getImages().stream()
                    .map(imageDTO -> Image.builder()
                            .url(imageDTO.getUrl())
                            .type(imageDTO.getType())
                            .build())
                    .toList());
        }

        productRepo.save(product);
    }



    @Override
    public void updateProduct(String sku, ProductDTO updatedProductDTO) {
        Product existingProduct = productRepo.findProductBySku(sku)
                .orElseThrow(() -> new NotFoundException("Product with SKU '" + sku + "' not found"));

        validateMandatoryFields(updatedProductDTO);

        Optional<Category> categoryOpt = validateAndRetrieveCategory(updatedProductDTO.getCategory());
        Optional<SubCategory> subCategoryOpt = validateAndRetrieveSubCategory(updatedProductDTO.getSubCategory(), updatedProductDTO.getCategory());
        Optional<ItemCategory> itemCategoryOpt = validateAndRetrieveItemCategory(updatedProductDTO.getItemCategory(), updatedProductDTO.getSubCategory(), updatedProductDTO.getCategory());

        existingProduct.setSku(updatedProductDTO.getSku());
        existingProduct.setName(updatedProductDTO.getName());
        existingProduct.setDescription(updatedProductDTO.getDescription());
        existingProduct.setCategory(categoryOpt.orElse(null));
        existingProduct.setSubCategory(subCategoryOpt.orElse(null));
        existingProduct.setItemCategory(itemCategoryOpt.orElse(null));
        existingProduct.setBroschure(updatedProductDTO.getBroschure());
        existingProduct.setTehnic(updatedProductDTO.getTehnic());
        existingProduct.setCatalog(updatedProductDTO.getCatalog());
        existingProduct.setLinkVideo(updatedProductDTO.getLinkVideo());

        if (updatedProductDTO.getImages() != null) {
            existingProduct.setImages(updatedProductDTO.getImages().stream()
                    .map(imageDTO -> Image.builder()
                            .url(imageDTO.getUrl())
                            .type(imageDTO.getType())
                            .build())
                    .toList());
        }

        productRepo.save(existingProduct);
    }


    @Override
    public void deleteProduct(String sku) {
        Product existingProduct = productRepo.findProductBySku(sku)
                .orElseThrow(() -> new NotFoundException("Product with SKU '" + sku + "' not found"));

        List<String> fileUrls = new ArrayList<>();

        if (existingProduct.getImages() != null) {
            fileUrls.addAll(existingProduct.getImages().stream()
                    .map(Image::getUrl)
                    .toList());
        }

        if (existingProduct.getBroschure() != null) {
            fileUrls.add(existingProduct.getBroschure());
        }
        if (existingProduct.getTehnic() != null) {
            fileUrls.add(existingProduct.getTehnic());
        }
        if (existingProduct.getCatalog() != null) {
            fileUrls.add(existingProduct.getCatalog());
        }

        try {
            if (!fileUrls.isEmpty()) {
                cloudAdapter.deleteFiles(fileUrls);
            }
        } catch (Exception e) {
            throw new AppException("Failed to delete associated documents from cloud");
        }

        productRepo.delete(existingProduct);
    }


    private void validateMandatoryFields(ProductDTO productDTO) {
        if (productDTO.getSku() == null || productDTO.getSku().trim().isEmpty()) {
            throw new AppException("SKU cannot be null or empty");
        }
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new AppException("Product name cannot be null or empty");
        }
        if (productDTO.getDescription() == null || productDTO.getDescription().trim().isEmpty()) {
            throw new AppException("Description cannot be null or empty");
        }
    }

    private Optional<Category> validateAndRetrieveCategory(String categoryName) {
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            return categoryRepo.findCategoryByName(categoryName)
                    .or(() -> {
                        throw new NotFoundException("Category with name '" + categoryName + "' not found");
                    });
        }
        return Optional.empty();
    }

    private Optional<ItemCategory> validateAndRetrieveItemCategory(String itemCategoryName, String subCategoryName, String categoryName) {
        if (itemCategoryName != null && !itemCategoryName.trim().isEmpty() && subCategoryName != null && categoryName != null) {
            return categoryRepo.findCategoryByName(categoryName)
                    .flatMap(category -> subCategoryRepo.findByNameAndCategory(subCategoryName, category)
                            .flatMap(subCategory -> itemCategoryRepo.findByNameAndSubCategoryAndCategory(itemCategoryName, subCategory, category)))
                    .or(() -> {
                        throw new NotFoundException("Item category with name '" + itemCategoryName + "' not found in subcategory '" + subCategoryName + "' within category '" + categoryName + "'");
                    });
        }
        return Optional.empty();
    }


    private Optional<SubCategory> validateAndRetrieveSubCategory(String subCategoryName, String categoryName) {
        if (subCategoryName != null && !subCategoryName.trim().isEmpty() && categoryName != null) {
            return categoryRepo.findCategoryByName(categoryName)
                    .flatMap(category -> subCategoryRepo.findByNameAndCategory(subCategoryName, category))
                    .or(() -> {
                        throw new NotFoundException("Subcategory with name '" + subCategoryName + "' not found in category '" + categoryName + "'");
                    });
        }
        return Optional.empty();
    }

}
