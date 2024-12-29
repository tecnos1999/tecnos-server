package com.example.tecnosserver.products.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
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
import com.example.tecnosserver.tags.model.Tag;
import com.example.tecnosserver.tags.repo.TagRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional
@Slf4j
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepo productRepo;
    private final ItemCategoryRepo itemCategoryRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final PartnerRepo partnerRepo;
    private final CloudAdapter cloudAdapter;
    private final TagRepo tagRepo;

    public ProductCommandServiceImpl(ProductRepo productRepo,
                                     ItemCategoryRepo itemCategoryRepo,
                                     CategoryRepo categoryRepo,
                                     SubCategoryRepo subCategoryRepo,
                                     PartnerRepo partnerRepo,
                                     CloudAdapter cloudAdapter, TagRepo tagRepo) {
        this.productRepo = productRepo;
        this.itemCategoryRepo = itemCategoryRepo;
        this.categoryRepo = categoryRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.partnerRepo = partnerRepo;
        this.cloudAdapter = cloudAdapter;
        this.tagRepo = tagRepo;
    }

    @Override
    public void createProduct(ProductDTO productDTO, List<MultipartFile> imageFiles,
                              MultipartFile broschureFile, MultipartFile tehnicFile) {
        validateMandatoryFields(productDTO);

        if (productRepo.findProductBySku(productDTO.getSku()).isPresent()) {
            throw new AlreadyExistsException("Product with SKU '" + productDTO.getSku() + "' already exists");
        }

        Category category = validateAndRetrieveCategory(productDTO.getCategory()).orElse(null);
        SubCategory subCategory = validateAndRetrieveSubCategory(productDTO.getSubCategory(), productDTO.getCategory()).orElse(null);
        ItemCategory itemCategory = validateAndRetrieveItemCategory(productDTO.getItemCategory(), productDTO.getSubCategory(), productDTO.getCategory()).orElse(null);

        Partner partner = productDTO.getPartnerName() != null
                ? partnerRepo.findByName(productDTO.getPartnerName())
                .orElseThrow(() -> new NotFoundException("Partner with name '" + productDTO.getPartnerName() + "' not found"))
                : null;

        String broschureUrl = broschureFile != null ? cloudAdapter.uploadFile(broschureFile) : null;
        String tehnicUrl = tehnicFile != null ? cloudAdapter.uploadFile(tehnicFile) : null;

        List<String> imageUrls = imageFiles != null
                ? imageFiles.stream().map(cloudAdapter::uploadFile).toList()
                : List.of();

        List<Tag> tags = productDTO.getTags() != null
                ? productDTO.getTags().stream().map(this::validateOrCreateTag).toList()
                : List.of();

        Product product = Product.builder()
                .sku(productDTO.getSku())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .category(category)
                .subCategory(subCategory)
                .itemCategory(itemCategory)
                .broschure(broschureUrl)
                .tehnic(tehnicUrl)
                .linkVideo(productDTO.getLinkVideo())
                .images(imageUrls)
                .partner(partner)
                .tags(tags)
                .build();

        productRepo.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(String sku, ProductDTO updatedProductDTO,
                              List<MultipartFile> newImageFiles,
                              MultipartFile newBroschureFile, MultipartFile newTehnicFile) {
        Product existingProduct = productRepo.findProductBySku(sku)
                .orElseThrow(() -> new NotFoundException("Product with SKU '" + sku + "' not found"));

        validateMandatoryFields(updatedProductDTO);

        try {
            // Ștergere imagini specifice
            if (updatedProductDTO.getImagesToRemove() != null && !updatedProductDTO.getImagesToRemove().isEmpty()) {
                updatedProductDTO.getImagesToRemove().forEach(imageUrl -> {
                    if (existingProduct.getImages().remove(imageUrl)) {
                        safeDeleteFile(imageUrl); // Șterge din cloud
                    }
                });

                // Forțează sincronizarea modificărilor cu baza de date
                productRepo.saveAndFlush(existingProduct);
            }

            // Adaugă imagini noi
            if (newImageFiles != null && !newImageFiles.isEmpty()) {
                List<String> newImageUrls = newImageFiles.stream()
                        .map(cloudAdapter::uploadFile)
                        .toList();
                existingProduct.getImages().addAll(newImageUrls);
            }

            // Elimină duplicatele
            existingProduct.setImages(existingProduct.getImages().stream().distinct().toList());

            // Actualizează alte detalii
            updateDocument(existingProduct.getBroschure(), newBroschureFile, existingProduct::setBroschure);
            updateDocument(existingProduct.getTehnic(), newTehnicFile, existingProduct::setTehnic);

            existingProduct.setName(updatedProductDTO.getName());
            existingProduct.setDescription(updatedProductDTO.getDescription());
            existingProduct.setCategory(validateAndRetrieveCategory(updatedProductDTO.getCategory()).orElse(null));
            existingProduct.setSubCategory(validateAndRetrieveSubCategory(updatedProductDTO.getSubCategory(), updatedProductDTO.getCategory()).orElse(null));
            existingProduct.setItemCategory(validateAndRetrieveItemCategory(updatedProductDTO.getItemCategory(), updatedProductDTO.getSubCategory(), updatedProductDTO.getCategory()).orElse(null));
            existingProduct.setTags(updatedProductDTO.getTags() != null
                    ? updatedProductDTO.getTags().stream().map(this::validateOrCreateTag).toList()
                    : List.of());
            existingProduct.setLinkVideo(updatedProductDTO.getLinkVideo());

            // Salvează modificările
            productRepo.save(existingProduct);

            log.info("Product with SKU '{}' was successfully updated.", sku);
        } catch (Exception ex) {
            log.error("Failed to update product with SKU '{}'. Rolling back changes.", sku, ex);
            throw new AppException("An error occurred while updating the product. Transaction rolled back.");
        }
    }





    @Override
    public void deleteProduct(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            throw new AppException("Product SKU cannot be null or empty.");
        }

        Product product = productRepo.findProductBySku(sku.trim())
                .orElseThrow(() -> new NotFoundException("Product with SKU '" + sku + "' not found."));

        if (product.getBroschure() != null) {
            safeDeleteFile(product.getBroschure());
        }
        if (product.getTehnic() != null) {
            safeDeleteFile(product.getTehnic());
        }
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            product.getImages().forEach(this::safeDeleteFile);
        }

        productRepo.delete(product);

        log.info("Product with SKU '{}' was successfully deleted.", sku);
    }


    private void safeDeleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                cloudAdapter.deleteFile(fileUrl);
                log.info("File successfully deleted: {}", fileUrl);
            } catch (Exception e) {
                log.error("Failed to delete file: {}", fileUrl, e);
            }
        }
    }


    private void validateMandatoryFields(ProductDTO productDTO) {
        if (productDTO.getSku() == null || productDTO.getSku().isBlank())
            throw new AppException("SKU cannot be null or empty");
        if (productDTO.getName() == null || productDTO.getName().isBlank())
            throw new AppException("Product name cannot be null or empty");
        if (productDTO.getDescription() == null || productDTO.getDescription().isBlank())
            throw new AppException("Description cannot be null or empty");
    }

    private Tag validateOrCreateTag(String tagName) {
        return tagRepo.findByName(tagName)
                .orElseGet(() -> tagRepo.save(Tag.builder().name(tagName).build()));
    }

    private void updateDocument(String existingUrl, MultipartFile newFile, Consumer<String> setUrl) {
        if (existingUrl != null) {
            cloudAdapter.deleteFile(existingUrl);
        }
        String newUrl = cloudAdapter.uploadFile(newFile);
        setUrl.accept(newUrl);
    }

    private Optional<Category> validateAndRetrieveCategory(String categoryName) {
        return categoryRepo.findCategoryByName(categoryName)
                .or(() -> {
                    throw new NotFoundException("Category with name '" + categoryName + "' not found");
                });
    }

    private Optional<SubCategory> validateAndRetrieveSubCategory(String subCategoryName, String categoryName) {
        return subCategoryRepo.findByNameAndCategory(subCategoryName, validateAndRetrieveCategory(categoryName).orElseThrow());
    }

    private Optional<ItemCategory> validateAndRetrieveItemCategory(String itemCategoryName, String subCategoryName, String categoryName) {
        return itemCategoryRepo.findByNameAndSubCategoryAndCategory(itemCategoryName,
                validateAndRetrieveSubCategory(subCategoryName, categoryName).orElseThrow(),
                validateAndRetrieveCategory(categoryName).orElseThrow());
    }
}
