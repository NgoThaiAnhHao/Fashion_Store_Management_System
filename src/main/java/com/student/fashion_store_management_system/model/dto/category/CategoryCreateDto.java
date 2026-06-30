package com.student.fashion_store_management_system.model.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDto {
    @NotBlank(message = "* Category name is mandatory!")
    @Size(min = 3, max = 30, message = "* Your category name must be at least 3 characters long!")
    private String name;

    private String description;

}
