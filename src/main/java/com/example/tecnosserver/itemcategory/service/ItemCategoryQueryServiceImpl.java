package com.example.tecnosserver.itemcategory.service;

import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.itemcategory.repo.ItemCategoryRepo;
import com.example.tecnosserver.subcategory.repo.SubCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


import com.example.tecnosserver.itemcategory.dto.ItemCategoryDTO;
import com.example.tecnosserver.itemcategory.mapper.ItemCategoryMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemCategoryQueryServiceImpl implements ItemCategoryQueryService {

    private final ItemCategoryRepo itemCategoryRepository;
    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;
    private final ItemCategoryMapper itemCategoryMapper;

    public ItemCategoryQueryServiceImpl(ItemCategoryRepo itemCategoryRepository,
                                        SubCategoryRepo subCategoryRepo,
                                        CategoryRepo categoryRepo,
                                        ItemCategoryMapper itemCategoryMapper) {
        this.itemCategoryRepository = itemCategoryRepository;
        this.subCategoryRepo = subCategoryRepo;
        this.categoryRepo = categoryRepo;
        this.itemCategoryMapper = itemCategoryMapper;
    }

    @Override
    public Optional<List<ItemCategoryDTO>> findAllItemCategories(String subCategoryName, String categoryName) {
        List<ItemCategory> itemCategories;

        if (subCategoryName != null && categoryName != null) {
            itemCategories = itemCategoryRepository.findAllBySubCategoryAndCategory(
                    subCategoryRepo.findByNameAndCategory(
                            subCategoryName,
                            categoryRepo.findCategoryByName(categoryName)
                                    .orElseThrow(() -> new NotFoundException("Category with name " + categoryName + " not found"))
                    ).orElseThrow(() -> new NotFoundException("SubCategory with name " + subCategoryName + " not found in category " + categoryName)),
                    categoryRepo.findCategoryByName(categoryName)
                            .orElseThrow(() -> new NotFoundException("Category with name " + categoryName + " not found"))
            );

            if (itemCategories.isEmpty()) {
                throw new NotFoundException("No item categories found for subcategory " + subCategoryName + " and category " + categoryName);
            }

        } else {
            itemCategories = itemCategoryRepository.findAll();
            if (itemCategories.isEmpty()) {
                throw new NotFoundException("No item categories found");
            }
        }

        List<ItemCategoryDTO> itemCategoryDTOs = itemCategories.stream()
                .map(itemCategoryMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(itemCategoryDTOs);
    }
}
