package com.student.fashion_store_management_system.model.dto.product;

import com.student.fashion_store_management_system.model.entity.Category;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {

    @NotBlank(message = "* Product name is mandatory!")
    @Size(min = 3, max = 30, message = "* Your product name must be at least 3 characters long!")
    private String name;

    private String description;

    @NotNull(message = "* Price must be not null")
    @Min(value = 0, message = "* Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "* Price must not exceed 99,999,999.99 and may have at most 2 decimal places")
    private BigDecimal price;

    private String imageUrl;

    @NotNull(message = "* Stock quantity must be not null")
    @Min(value = 0, message = "* Stock quantity be greater than 0")
    @Max(value = 999, message = "* Stock quantity be least than 999")
    private Integer stockQuantity;

    @Min(value = 0, message = "* Discount percent be greater than 0")
    @Max(value = 100, message = "* Discount percent be least than 100")
    private Integer discountPercent;

    @NotNull(message = "Please select a category.")
    private Long categoryId; // Changed from long to Long
}
