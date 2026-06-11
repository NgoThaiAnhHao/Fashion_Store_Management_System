package com.student.fashion_store_management_system.model.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryCreateDto {
    @NotBlank(message = "* Category name is mandatory!")
    @Size(min = 3, max = 30, message = "* Your category name must be at least 3 characters long!")
    private String name;

    private String description;

    public CategoryCreateDto() {
    }

    public CategoryCreateDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
