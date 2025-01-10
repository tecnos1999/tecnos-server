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

        checkIfSkuExists(productDTO.getSku());

        Product product = initializeProduct(productDTO, broschureFile, tehnicFile);

        processImages(imageFiles, product);

        productRepo.saveAndFlush(product);

        log.info("Product with SKU '{}' was successfully created.", productDTO.getSku());
    }

    @Override
    public void deleteProduct(String sku) {
        validateSku(sku);

        Product product = retrieveProductBySku(sku.trim());

        deleteDocuments(product);
        deleteImages(product);

        productRepo.delete(product);

        log.info("Product with SKU '{}' was successfully deleted.", sku);
    }

    @Override
    @Transactional
    public void updateProduct(String sku, ProductDTO updatedProductDTO,
                              List<MultipartFile> newImageFiles,
                              MultipartFile newBroschureFile, MultipartFile newTehnicFile) {
        Product existingProduct = retrieveProductBySku(sku);

        validateMandatoryFields(updatedProductDTO);

        processImagesToRemove(updatedProductDTO.getImagesToRemove(), existingProduct);
        processNewImages(newImageFiles, existingProduct);
        updateDocuments(existingProduct, newBroschureFile, newTehnicFile);
        updateProductFields(existingProduct, updatedProductDTO);

        productRepo.saveAndFlush(existingProduct);

        log.info("Product with SKU '{}' was successfully updated.", sku);
    }


    private void validateMandatoryFields(ProductDTO productDTO) {
        if (productDTO.getSku() == null || productDTO.getSku().isBlank())
            throw new AppException("SKU cannot be null or empty");
        if (productDTO.getName() == null || productDTO.getName().isBlank())
            throw new AppException("Product name cannot be null or empty");
        if (productDTO.getDescription() == null || productDTO.getDescription().isBlank())
            throw new AppException("Description cannot be null or empty");
    }

    private void checkIfSkuExists(String sku) {
        if (productRepo.findProductBySku(sku).isPresent()) {
            throw new AlreadyExistsException("Product with SKU '" + sku + "' already exists");
        }
    }

    private Product initializeProduct(ProductDTO productDTO, MultipartFile broschureFile, MultipartFile tehnicFile) {
        Category category = validateAndRetrieveCategory(productDTO.getCategory()).orElse(null);
        SubCategory subCategory = validateAndRetrieveSubCategory(productDTO.getSubCategory(), productDTO.getCategory()).orElse(null);
        ItemCategory itemCategory = validateAndRetrieveItemCategory(productDTO.getItemCategory(), productDTO.getSubCategory(), productDTO.getCategory()).orElse(null);

        Partner partner = resolvePartner(productDTO.getPartnerName());

        String broschureUrl = processDocument(broschureFile);
        String tehnicUrl = processDocument(tehnicFile);

        return Product.builder()
                .sku(productDTO.getSku())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .category(category)
                .subCategory(subCategory)
                .itemCategory(itemCategory)
                .broschure(broschureUrl)
                .tehnic(tehnicUrl)
                .linkVideo(productDTO.getLinkVideo())
                .partner(partner)
                .build();
    }

    private void processImages(List<MultipartFile> imageFiles, Product product) {
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imageUrls = imageFiles.stream()
                    .map(cloudAdapter::uploadFile)
                    .filter(url -> url != null && !url.isBlank())
                    .toList();
            product.addImages(imageUrls);
        }
    }

    private Partner resolvePartner(String partnerName) {
        if (partnerName != null) {
            return partnerRepo.findByName(partnerName)
                    .orElseThrow(() -> new NotFoundException("Partner with name '" + partnerName + "' not found"));
        }
        return null;
    }

    private String processDocument(MultipartFile file) {
        return (file != null && !file.isEmpty()) ? cloudAdapter.uploadFile(file) : null;
    }

    private void deleteDocuments(Product product) {
        safeDeleteFile(product.getBroschure());
        safeDeleteFile(product.getTehnic());
    }

    private void deleteImages(Product product) {
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            product.getImages().forEach(image -> safeDeleteFile(image.getImageUrl()));
        }
    }

    private void processImagesToRemove(List<String> imagesToRemove, Product product) {
        if (imagesToRemove != null && !imagesToRemove.isEmpty()) {
            product.removeImages(imagesToRemove);
            imagesToRemove.forEach(this::safeDeleteFile);
        }
    }

    private void processNewImages(List<MultipartFile> newImageFiles, Product product) {
        if (newImageFiles != null && !newImageFiles.isEmpty()) {
            List<String> newImageUrls = newImageFiles.stream()
                    .map(cloudAdapter::uploadFile)
                    .filter(url -> url != null && !url.isBlank())
                    .toList();
            product.addImages(newImageUrls);
        }
    }

    private void updateDocuments(Product product, MultipartFile newBroschureFile, MultipartFile newTehnicFile) {
        if (newBroschureFile != null && !newBroschureFile.isEmpty()) {
            updateDocument(product.getBroschure(), newBroschureFile, product::setBroschure);
        }
        if (newTehnicFile != null && !newTehnicFile.isEmpty()) {
            updateDocument(product.getTehnic(), newTehnicFile, product::setTehnic);
        }
    }

    private void updateProductFields(Product product, ProductDTO updatedProductDTO) {
        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setLinkVideo(updatedProductDTO.getLinkVideo());
        product.setCategory(validateAndRetrieveCategory(updatedProductDTO.getCategory()).orElse(null));
        product.setSubCategory(validateAndRetrieveSubCategory(updatedProductDTO.getSubCategory(), updatedProductDTO.getCategory()).orElse(null));
        product.setItemCategory(validateAndRetrieveItemCategory(updatedProductDTO.getItemCategory(), updatedProductDTO.getSubCategory(), updatedProductDTO.getCategory()).orElse(null));
        product.setPartner(resolvePartner(updatedProductDTO.getPartnerName()));
        updateTags(product, updatedProductDTO.getTags());
    }

    private void updateTags(Product product, List<String> updatedTagNames) {
        if (updatedTagNames != null) {
            List<Tag> currentTags = product.getTags();

            List<Tag> tagsToRemove = currentTags.stream()
                    .filter(existingTag -> !updatedTagNames.contains(existingTag.getName()))
                    .toList();
            currentTags.removeAll(tagsToRemove);

            List<Tag> tagsToAdd = updatedTagNames.stream()
                    .map(this::validateExistingTag)
                    .filter(newTag -> currentTags.stream().noneMatch(existingTag -> existingTag.getName().equals(newTag.getName())))
                    .toList();
            currentTags.addAll(tagsToAdd);

            product.setTags(currentTags);
        }
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

    private Tag validateExistingTag(String tagName) {
        return tagRepo.findByName(tagName)
                .orElseThrow(() -> new NotFoundException("Tag with name '" + tagName + "' not found"));
    }

    private void updateDocument(String existingUrl, MultipartFile newFile, Consumer<String> setUrl) {
        if (existingUrl != null) {
            safeDeleteFile(existingUrl);
        }
        String newUrl = cloudAdapter.uploadFile(newFile);
        setUrl.accept(newUrl);
    }

    private void validateSku(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            throw new AppException("Product SKU cannot be null or empty.");
        }
    }

    private Product retrieveProductBySku(String sku) {
        return productRepo.findProductBySku(sku)
                .orElseThrow(() -> new NotFoundException("Product with SKU '" + sku + "' not found."));
    }

}
