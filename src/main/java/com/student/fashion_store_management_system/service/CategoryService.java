package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.dto.category.CategoryCreateDto;
import com.student.fashion_store_management_system.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    List<Category> findByName(String name);

    Category findById(long id);

    Category addNew(CategoryCreateDto categoryCreateDto);

    Category update(CategoryCreateDto categoryCreateDto, long id);

    void delete(long id);
}
