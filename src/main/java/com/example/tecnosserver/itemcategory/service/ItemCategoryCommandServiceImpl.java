package com.example.tecnosserver.itemcategory.service;

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

    public ItemCategoryCommandServiceImpl(ItemCategoryRepo itemCategoryRepository, SubCategoryRepo subCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    @Transactional
    public void createItemCategory(String name, String subCategoryName) {
        SubCategory subCategory = subCategoryRepository.findSubCategoryByName(subCategoryName)
                .orElseThrow(() -> new NotFoundException("SubCategory with name " + subCategoryName + " not found"));

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setName(name);
        itemCategory.setSubCategory(subCategory);
        itemCategoryRepository.save(itemCategory);
    }

    @Override
    @Transactional
    public void updateItemCategory(String name, String updatedName) {
        ItemCategory itemCategory = itemCategoryRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("ItemCategory with name " + name + " not found"));
        itemCategory.setName(updatedName);
        itemCategoryRepository.save(itemCategory);
    }

    @Override
    @Transactional
    public void deleteItemCategory(String name) {
        ItemCategory itemCategory = itemCategoryRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("ItemCategory with name " + name + " not found"));
        itemCategoryRepository.delete(itemCategory);
    }
}
