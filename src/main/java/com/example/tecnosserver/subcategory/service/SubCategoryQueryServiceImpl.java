package com.example.tecnosserver.subcategory.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.subcategory.dto.SubCategoryDTO;
import com.example.tecnosserver.subcategory.mapper.SubCategoryMapper;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.subcategory.repo.SubCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubCategoryQueryServiceImpl implements SubCategoryQueryService {

    private final SubCategoryRepo subCategoryRepo;
    private final SubCategoryMapper subCategoryMapper;

    public SubCategoryQueryServiceImpl(SubCategoryRepo subCategoryRepo, SubCategoryMapper subCategoryMapper) {
        this.subCategoryRepo = subCategoryRepo;
        this.subCategoryMapper = subCategoryMapper;
    }

    @Override
    public Optional<List<SubCategoryDTO>> findAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepo.findAll();
        if (subCategories.isEmpty()) {
            throw new NotFoundException("No sub categories found");
        }

        List<SubCategoryDTO> subCategoryDTOs = subCategories.stream()
                .map(subCategoryMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(subCategoryDTOs);
    }
}
