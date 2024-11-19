package com.example.tecnosserver.itemcategory.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.itemcategory.repo.ItemCategoryRepo;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.subcategory.repo.SubCategoryRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemCategoryCommandServiceImpl implements ItemCategoryCommandService {

    private final ItemCategoryRepo itemCategoryRepository;
    private final SubCategoryRepo subCategoryRepository;

    private final CategoryRepo categoryRepository;

    public ItemCategoryCommandServiceImpl(ItemCategoryRepo itemCategoryRepository, SubCategoryRepo subCategoryRepository, CategoryRepo categoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void createItemCategory(String name, String subCategoryName, String categoryName) {
        Category category = categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new NotFoundException("Category with name '" + categoryName + "' not found"));

        SubCategory subCategory = subCategoryRepository.findByNameAndCategory(subCategoryName, category)
                .orElseThrow(() -> new NotFoundException("SubCategory with name '" + subCategoryName + "' not found in category '" + categoryName + "'"));

        boolean itemCategoryExists = itemCategoryRepository.existsByNameAndSubCategoryAndCategory(name, subCategory, category);
        if (itemCategoryExists) {
            throw new NotFoundException("ItemCategory with name '" + name + "' already exists in subcategory '" + subCategoryName + "' within category '" + categoryName + "'");
        }

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setName(name);
        itemCategory.setSubCategory(subCategory);
        itemCategory.setCategory(category);

        itemCategoryRepository.save(itemCategory);
    }




    @Override
    @Transactional
    public void updateItemCategory(
            String name,
            String updatedName,
            String subCategoryName,
            String categoryName,
            String updatedSubCategoryName,
            String updatedCategoryName) {

        Category currentCategory = categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new NotFoundException("Category with name '" + categoryName + "' not found"));

        SubCategory currentSubCategory = subCategoryRepository.findByNameAndCategory(subCategoryName, currentCategory)
                .orElseThrow(() -> new NotFoundException("SubCategory with name '" + subCategoryName + "' not found in category '" + categoryName + "'"));

        Category updatedCategory = categoryRepository.findCategoryByName(updatedCategoryName)
                .orElseThrow(() -> new NotFoundException("Updated Category with name '" + updatedCategoryName + "' not found"));

        SubCategory updatedSubCategory = subCategoryRepository.findByNameAndCategory(updatedSubCategoryName, updatedCategory)
                .orElseThrow(() -> new NotFoundException("Updated SubCategory with name '" + updatedSubCategoryName + "' not found in category '" + updatedCategoryName + "'"));

        boolean itemCategoryExists = itemCategoryRepository.existsByNameAndSubCategoryAndCategory(updatedName, updatedSubCategory, updatedCategory);
        if (itemCategoryExists) {
            throw new NotFoundException("ItemCategory with name '" + updatedName + "' already exists in subcategory '" + updatedSubCategoryName + "' within category '" + updatedCategoryName + "'");
        }

        ItemCategory itemCategory = itemCategoryRepository.findByNameAndSubCategoryAndCategory(name, currentSubCategory, currentCategory)
                .orElseThrow(() -> new NotFoundException("ItemCategory with name '" + name + "' not found in subcategory '" + subCategoryName + "' within category '" + categoryName + "'"));

        itemCategory.setName(updatedName);
        itemCategory.setSubCategory(updatedSubCategory);
        itemCategory.setCategory(updatedCategory);

        itemCategoryRepository.save(itemCategory);
    }





    @Override
    @Transactional
    public void deleteItemCategory(String name, String subCategoryName, String categoryName) {
        Category category = categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new NotFoundException("Category with name '" + categoryName + "' not found"));

        SubCategory subCategory = subCategoryRepository.findByNameAndCategory(subCategoryName, category)
                .orElseThrow(() -> new NotFoundException("SubCategory with name '" + subCategoryName + "' not found in category '" + categoryName + "'"));

        ItemCategory itemCategory = itemCategoryRepository.findByNameAndSubCategoryAndCategory(name, subCategory, category)
                .orElseThrow(() -> new NotFoundException("ItemCategory with name '" + name + "' not found in subcategory '" + subCategoryName + "' within category '" + categoryName + "'"));

        itemCategoryRepository.delete(itemCategory);
    }




}
