package com.student.fashion_store_management_system.mapper;

import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.dto.category.CategoryCreateDto;
import com.student.fashion_store_management_system.model.entity.Category;
import com.student.fashion_store_management_system.model.entity.User;

public class CategoryMapper {
    public static Category toEntity(CategoryCreateDto categoryCreateDto) {
        return new Category(
                categoryCreateDto.getName(),
                categoryCreateDto.getDescription()
        );
    }
}
