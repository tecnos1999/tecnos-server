package com.example.tecnosserver.itemcategory.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.itemcategory.repo.ItemCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCategoryQueryServiceImpl implements ItemCategoryQueryService {

    private final ItemCategoryRepo itemCategoryRepository;

    public ItemCategoryQueryServiceImpl(ItemCategoryRepo itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }

    @Override
    public Optional<ItemCategory> findItemCategoryByName(String name) {
        Optional<ItemCategory> itemCategory = itemCategoryRepository.findByName(name);
        if (itemCategory.isEmpty()) {
            throw new NotFoundException("Item category with name " + name + " not found");
        }
        return itemCategory;
    }

    @Override
    public Optional<List<ItemCategory>> findAllItemCategories() {
        List<ItemCategory> itemCategories = itemCategoryRepository.findAll();
        if (itemCategories.isEmpty()) {
            throw new NotFoundException("No item categories found");
        }
        return Optional.of(itemCategories);
    }
}
