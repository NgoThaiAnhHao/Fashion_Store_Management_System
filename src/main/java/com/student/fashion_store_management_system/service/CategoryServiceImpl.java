package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.exception.common.DuplicateCategoryException;
import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.mapper.CategoryMapper;
import com.student.fashion_store_management_system.model.dto.category.CategoryCreateDto;
import com.student.fashion_store_management_system.model.entity.Category;
import com.student.fashion_store_management_system.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("CATEGORY NOT FOUND")
                );
    }

    @Override
    @Transactional
    public Category addNew(CategoryCreateDto categoryCreateDto) {
        if (isCategoryExist(categoryCreateDto.getName())) {
            throw new DuplicateCategoryException("CATEGORY ALREADY EXISTS");
        }

        return categoryRepository.save(
                CategoryMapper.toEntity(categoryCreateDto)
        );
    }

    @Override
    @Transactional
    public Category update(CategoryCreateDto categoryCreateDto, long id) {
        // Check duplicate category name
        if (isCategoryExist(categoryCreateDto.getName())
                && !categoryCreateDto.getName().equals(findById(id).getName())) {
            throw new DuplicateCategoryException("CATEGORY ALREADY EXISTS");
        }

        // Update fields
        Category category = findById(id);
        category.setName(categoryCreateDto.getName());
        category.setDescription(categoryCreateDto.getDescription());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(long id) {
        categoryRepository.deleteById(id);
    }

    private boolean isCategoryExist(String categoryName) {
        return categoryRepository.findByName(categoryName).isPresent();
    }
}
